package com.example.jpashopp.repository;

import com.example.jpashopp.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId); //현재 로그인한 회원의 Cart 엔티티를 찾기 위해서 쿼리 메소드를 추가
}
