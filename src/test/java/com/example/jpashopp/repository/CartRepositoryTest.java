package com.example.jpashopp.repository;

import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.dto.MemberFormDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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


}