package com.example.jpashopp.domain.orders;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemSellStatus;
import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.repository.ItemRepository;
import com.example.jpashopp.repository.MemberRepository;
import com.example.jpashopp.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;


    public Item createItem(){
        Item item = Item.builder()
                .itemNm("테스트상품")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();

        IntStream.rangeClosed(1,3).forEach(i->{
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);//아직 영속성 컨텍스트에 저장되지 않은 orerItem 엔티티를 order 엔티티에 담아줍니다.
        });

        orderRepository.saveAndFlush(order);//order 엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영합니다.
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());//temOrder 엔티티 3개가 실제로 데이터베이스에 저장되었는지를 검사합니다.
    }



    public Order createOrder() {
        Order order = new Order();

        // 1부터 3까지(1, 2, 3 포함) 연속된 정수 스트림을 생성합니다. 즉, 이 스트림은 1, 2, 3의 값을 순차적으로 가진다
        IntStream.rangeClosed(1, 3).forEach(i -> { //스트림에서 하나씩 가져온 값(1, 2, 3)이 순차적으로 i에 할당
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);
        });

        Member member = new Member();
        memberRepository.save(member);

        Order.builder()
                .member(member)
                .build();
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0); //order.getOrderItems().remove(0) : order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 제거합니다.
        em.flush();//flush()를 호출하면 콘솔창에 orderItem을 삭제하는 쿼리문이 출력되는 것을 확인할 수 있습니다. 즉, 부모 엔티티와 연관 관계가 끊어졌기 때문에 고아 객체를 삭제하는 쿼리문이 실행되는 것입니다.
    }

}