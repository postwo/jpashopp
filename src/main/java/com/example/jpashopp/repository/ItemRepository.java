package com.example.jpashopp.repository;

import com.example.jpashopp.domain.items.Item;
import com.example.jpashopp.domain.items.ItemRepositoryCustom;
import com.example.jpashopp.dto.ItemSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/*<S extends T> save(S entity): 엔티티 저장 및 수정
void delete(T entity): 엔티티 삭제
count(): 엔티티 총 개수 반환
Iterable<T> findAll(): 모든 엔티티 조회*/

public interface ItemRepository extends JpaRepository<Item,Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom     {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}

