package com.example.jpashopp.repository;

import com.example.jpashopp.domain.cart.Cart;
import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.dto.MemberFormDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    public Member createmaber(){
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@email.com")
                .name("테스트")
                .address("서울시 강서구")
                .password("1111")
                .build();

        return Member.createMember(memberFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("장바구니 회원 entity 매핑 조회 테스트")
    public void findCartAndMemberTest(){
        Member member  = createmaber();
        memberRepository.save(member);

        Cart cart = Cart.builder()
                .member(member)
                .build();
        cartRepository.save(cart);

        /*JPA는 영속성 컨텍스트에 데이터를 저장 후 트랜잭션이 끝날 때 flush()를 호출하여 데이터베이스에 반영합니다.
        위에서는 회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저(em)로부터 강제로 flush()를
        호출하여 데이터베이스에 반영*/
        em.flush(); // 영속성 컨텍스트에 있는 변경 내용을 데이터베이스에 반영


        /* JPA는 영속성 컨텍스트로부터 엔티티를 조회 후 영속성 컨텍스트에 엔티티가 없을 경우 데이터베이스를 조회합니다.
         실제 데이터베이스에서 장바구니 엔티티를 가지고 올 때 회원 엔티티도 같이 가지고 오는지를 확인하기 위해 영속성 컨텍스트를 비워준다*/
        em.clear();//영속성 컨텍스트를 비웁니다. 이렇게 하면 이후의 조회는 데이터베이스에서 직접 이루어진다

        Cart savedcart = cartRepository.findById(cart.getId()) //코드를 실행할 때는 cart 테이블과 member 테이블을 조인해서 가져오는 쿼리문이 실행됩니다. cart 엔티티를 조회하면서 member 엔티티도 동시에 가져오는
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedcart.getMember().getId(), member.getId());
    }


}