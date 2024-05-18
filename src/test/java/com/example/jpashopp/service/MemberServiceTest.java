package com.example.jpashopp.service;


import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.domain.members.MemberRole;
import com.example.jpashopp.dto.MemberFormDto;
import com.example.jpashopp.repository.MemberRepository;
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
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = MemberFormDto.builder()
                .email("test@email.com")
                .name("테스트")
                .address("서울시 강서구")
                .password("1111") // 비밀번호는 아직 평문으로 설정됨
                .build();
        return Member.createMember(memberFormDto, passwordEncoder); // 여기서 비밀번호가 암호화됨
    }





    @Test
    @DisplayName("회원가입 테스트")
    void name() {
        Member member = createMember();

        Member savedMember = memberService.membersave(member);

        assertEquals(member.getEmail(),savedMember.getEmail());
    }
}