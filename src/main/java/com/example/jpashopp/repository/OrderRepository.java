package com.example.jpashopp.repository;

import com.example.jpashopp.domain.orders.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*@Query 어노테이션을 이용하여 주문 이력을 조회하는 쿼리를 작성하겠습니다.
     조회 조건이 복잡하지 않으면 QueryDsl을 사용하지 않고 @Query 어노테이션을 이용해서 구현하는 것도 괜찮다고 생각*/
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.orderDate desc")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}