package com.example.jpashopp.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    /*Spring Data Jpa에서는 Auditing 기능을 제공하여 엔티티가 저장 또는 수정될 때 자동으로 등록일, 수정일, 등록자, 수정자를 입력해준다.
    공통 멤버 변수들을 추상 클래스로 만들고 해당 추상 클래스를 상속받는 형태로 엔티티를 리팩토링
    * */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if (authentication != null) {
            userId = authentication.getName();//현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
        }

        return Optional.of(userId);
    }
}