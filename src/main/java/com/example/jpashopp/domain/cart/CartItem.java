package com.example.jpashopp.domain.cart;

import com.example.jpashopp.domain.BaseEntity;
import com.example.jpashopp.domain.items.Item;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;




@NoArgsConstructor
@Getter
@Table(name = "cart_item")
@Entity
public class CartItem extends BaseEntity {

    /*하나의 장바구니에는 여러 개의 상품들이 들어갈 수 있습니다. 또한 같은 상품을 여러 개 주문할 수
     있으므로 몇 개 담아 줄 것인지도 설정해줘야 합
    */

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id") //엔티티와 매핑되는 테이블에 @JoinColumn 어노테이션의 name으로 설정한 값이 외래키(FK)로 추가된 것을 볼 수 있다
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    @Builder
    public CartItem(Cart cart, Item item, int count) {
        this.cart = cart;
        this.item = item;
        this.count = count;
    }
}