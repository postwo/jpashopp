package com.example.jpashopp.repository;

import com.example.jpashopp.domain.members.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //회원 가입시 중복된 회원이 있는지 검사하기 위해 이메일로 회원을 검사하는 메소드를 작성
    Member findByEmail(String email);
}
