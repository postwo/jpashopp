package com.example.jpashopp.service;

import com.example.jpashopp.domain.members.Member;
import com.example.jpashopp.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional //로직을 처리하다가 에러가 발생하면, 변경된 데이터를 로직을 수행하기 이전 상태로 콜백
@Service
public class MemberService implements UserDetailsService { //UserDetailsService 로그인 때문에 활용해야함

    private final MemberRepository memberRepository;

    //회원가입
    public Member membersave(Member member){
        validateDuplicateMember(member);

        return memberRepository.save(member);

    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null){
            throw new IllegalArgumentException("이미 가입된 회원입니다");
        }
    }



    //로그인
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
