package com.example.jpashopp.domain.orders;

import com.example.jpashopp.domain.BaseEntity;
import com.example.jpashopp.domain.members.Member;
import groovyjarjarantlr.collections.impl.BitSet;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @ToString
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order extends BaseEntity { //양항뱡 매핑이란 단방향 매핑이 2개 있다고 생각하시면 됩니다. 주문과 주문 상품의 매핑을 양방향으로 설정
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; //한 명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑

    private LocalDateTime orderDate;    //주문일




    /*테이블은 외래키 하나로 양방향 조회가 가능하지만, 엔티티는 테이블과 다릅니다. 엔티티를 양방향 연관 관계로
    설정하면 객체의 참조는 둘인데 외래키는 하나이기 때문에 둘 중 누가 외래키를 관리할지를 정해야 합니다.
    1. 연관 관계의 주인은 외래키가 있는 곳으로 설정
    2. 연관 관계의 주인이 외래키를 관리(등록, 수정, 삭제)
    3. 주인이 아닌 쪽은 연관 관계 매핑 시 mapperdBy 속성의 값으로 연관 관계의 주인을 설정
    4. 주인이 아닌 쪽은 읽기만 가능*/

    /*@OneToMany(mappedBy = "order") : 주문 상품 엔티티와 일대다 매핑을 합니다. 외래키(order_id)가 order_item 테이블에
    있으므로 연관 관계의 주인은 OrderItem 엔티티입니다. Order 엔티티가 주인이 아니므로 “mappedBy” 속성으로 연관 관계의 주인 설정을 합니다.

    무조건 양방향으로 연관 관계를 매핑하면 해당 엔티티는 엄청나게 많은 테이블과 연관 관계를 맺게 되고 엔티티 클래스 자체가 복잡해지기
    때문에 연관 관계 단방향 매핑으로 설계 후 필요한 경우에 양방향 매핑을 추가하는 것을 권장*/


    /*영속성 전이(cascade)란 엔티티의 상태를 변경할 떄 해당 엔티티와 연관된 엔티티의 상태 변화를 전파하는 옵션입니다.
    이때 부모는 One에 해당하고 해당 자식은 Many에 해당합니다. 영속성 전이 옵션을 부분별하게 사용할 경우 삭제되지 말아야 할
    데이터가 삭제될 수 있으므로 조심해서 사용해야 합니다.(엔티티 간의 라이프사이클을 잘잘 파악할 것!)

    CASCADE 종류	설명
    PERSIST	부모 엔티티가 영속화될 때 자식 엔티티도 영속화
    MERGE	부모 엔티티가 병합될 떄 자식 엔티티도 병합
    REMOVE	부모 엔티티가 삭제될 때 연관된 자식 엔티티도 삭제
    REFRESH	부모 엔티티가 refresh되면 연관된 자식 엔티티도 refresh
    DETACH	부모 엔티티가 detach 되면 연관된 자식 엔티티도 detach 상태로 변경
    ALL	부모 엔티티의 영속 상태 변화를 자신 엔티티에 모두 전이*/


    /*고아 객체 제거하기 = orphanRemoval = true
    고아 객체란 부모 엔티티와 연관 관계가 끊어진 자식 엔티티를 말합니다. 영속성 전이 기능과 같이 사용하면 부모 엔티티를 통해서
    자식의 생명 주기를 관리할 수 있습니다. 고아 객체 제거 기능은 참조하는 곳이 하나일 때만 사용해야 합니다. 다른 곳에서도 참조하고
    있는 엔티티인데 삭제하면 문제가 생길 수 있기 때문*/
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();




    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    //주문상태



    @Builder
    public Order(Member member, LocalDateTime orderDate, List<OrderItem> orderItems, OrderStatus orderStatus) {
        this.member = member;
        this.orderDate = orderDate;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
        this.orderStatus = orderStatus;
    }





    //생성한 주문 상품 객체를 이용하여 주문 객체를 만드는 메소드를 작성

    public void addOrderItem(OrderItem orderItem) { //Order 엔티티와 OrderItem 엔티티가 양방향 참조 관계이므로, orderItem 객체에도 order 객체를 셋팅
        orderItems.add(orderItem);
        orderItem = OrderItem.builder()
                .order(this)
                .build();
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .orderDate(LocalDateTime.now())
                .build();

        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }

        return totalPrice;
    }


    public void cancelOrder() {//Item 클래스에 주문 취소시 주문 수량을 상품의 재고에 더해주는 로직과 주문 상태를 취소 상태로 바꿔주는 메소드를 구현
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }







}
