package com.example.jpashopp.domain.items;

import com.example.jpashopp.domain.BaseEntity;
import com.example.jpashopp.dto.ItemFormDto;
import com.example.jpashopp.exception.OutOfStockException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Entity
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id; //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    @Column(name = "price", nullable = false)
    private int price;  //가격

    @Column(nullable = false)
    private int stockNumber;  //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;  //상품 상세설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;  //상품 판매 상태

    @Builder
    public Item(String itemNm, int price, int stockNumber, String itemDetail, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.stockNumber =stockNumber;
        this.itemDetail = itemDetail;
        this.itemSellStatus = itemSellStatus;
    }


    /*엔티티 클래스에 비즈니스 로직을 추가한다면 조금 더 객체지향적으로 코딩을 할 수 있고,
    코드를 재활용할 수 있습니다. 또한 데이터 변경 포인트를 한군데에서 관리할 수 있습니다.
    * */
    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }


    public void removeStock(int stockNumber) { //상품을 주문할 경우 상품의 재고를 감소시키는 로직을 작성
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }
}
