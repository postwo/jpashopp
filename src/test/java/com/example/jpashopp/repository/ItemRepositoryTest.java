package com.example.jpashopp.repository;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemSellStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //통합 테스트를 위해 스프링부트에서 제공하는 어노테이션
///*테스트 코드 실행시 application.yml에 설정해둔 값보다 application-test.yml에 같은 설정이 있다면 더 높은 우선순위를 부여합니다.
// 기존에는 MySQL을 사용했지만 테스트 코드 실행 시에는 H2 데이터베이스를 사용하게 됩니다.*/
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("상품 저장 테스트")//Junit5에 추가된 어노테이션으로 테스트코드 실행시 지정한 테스트명이 노출됩니다.
    @Test
    void createItemTest() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("테스트 상품 상세설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();
        Item savedItem = itemRepository.save(item);
        System.out.println("save 잘들어갔는지" + savedItem);
        assertNotNull(savedItem);
    }
}