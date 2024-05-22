package com.example.jpashopp.repository;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemImg;
import com.example.jpashopp.domain.items.ItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); //이미지가 잘 저장됐는지 테스트 코드를 작성하기 위해추가
}