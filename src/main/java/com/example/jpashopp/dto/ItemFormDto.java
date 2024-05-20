package com.example.jpashopp.dto;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemSellStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세 내용은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 저장 후 수정할 때 상품 이미지 정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>(); //상품의 이미지 아이디를 저장하는 리스트입니다. 수정 시에 이미지 아이디를 담아둘 용도로 사용

    @Builder
    public ItemFormDto(String itemNm, Integer price, String itemDetail, Integer stockNumber, ItemSellStatus itemSellStatus) {
        this.itemNm = itemNm;
        this.price = price;
        this.itemDetail = itemDetail;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
    }

    public Item toEntity(ItemFormDto dto) { //dto를 엔티티로 변환하는 작업을 위해 만든 메소드
        Item entity = Item.builder()
                .itemNm(dto.itemNm)
                .itemDetail(dto.itemDetail)
                .itemSellStatus(dto.itemSellStatus)
                .price(dto.price)
                .stockNumber(dto.stockNumber)
                .build();

        return entity;
    }

    public ItemFormDto of(Item entity) { //엔티티를 DTO로 변환하는 작업을 위해 만든 메소드
        ItemFormDto dto = ItemFormDto.builder()
                .itemNm(entity.getItemNm())
                .itemDetail(entity.getItemDetail())
                .itemSellStatus(entity.getItemSellStatus())
                .price(entity.getPrice())
                .stockNumber(entity.getStockNumber())
                .build();

        return dto;
    }
}