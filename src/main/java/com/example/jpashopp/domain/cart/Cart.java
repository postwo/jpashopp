package com.example.jpashopp.domain.cart;

import com.example.jpashopp.domain.members.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "cart")
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    /*@JoinColumn: 매핑할 외래키를 지정합니다. @JoinColumn의 name을 명시하지 않으면
    JPA가 알아서 찾지만 컬럼명이 원하는 대로 생성되지 않을 수 있기 때문에 직접 지정*/

    /*이렇게 매핑을 맺어주면 장바구니 엔티티를 조회하면서 회원 엔티티의 정보도 동시에 가져올 수 있는 장점*/

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Cart(Member member) {
        this.member = member;
    }
}