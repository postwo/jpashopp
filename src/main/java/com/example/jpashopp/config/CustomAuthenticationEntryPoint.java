package com.example.jpashopp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;



public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*만약 인증되지 않은 사용자(401)가 리소스를 요청할 경우 “Unauthorized” 에러를 발생하도록
    config 패키지 하위에 AuthenticationEntryPoint 인터페이스를 구현한다.*/

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//    }



    //일반 사용자가 관리자 권한 페이지에 접근할 때 403 Forbidden 에러
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
    }

}