package com.example.jpashopp.domain.orders;

import com.example.jpashopp.domain.BaseEntity;
import com.example.jpashopp.domain.items.Item;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor
@Table(name = "order_item")
@Entity
public class OrderItem  extends BaseEntity { //주문 상품 엔티티와 주문 엔티티의 단방향 매핑

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;


    /*즉시 로딩을 사용하면 사용하지 않는 데이터도 한꺼번에 조회하므로
         성능 이슈가 발생할 수 있기 때문에 지연 로딩 방식을 사용해야 합니다.
         프로젝트 내에 모든 연관 관계를 FetchType.LAZY 방식으로 설정
    */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  //주문가격

    private int count;  //수량

    @Builder
    public OrderItem(Item item, Order order, int orderPrice, int count) {
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
