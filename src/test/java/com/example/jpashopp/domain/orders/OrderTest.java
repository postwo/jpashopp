package com.example.jpashopp.domain.orders;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemSellStatus;
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


}