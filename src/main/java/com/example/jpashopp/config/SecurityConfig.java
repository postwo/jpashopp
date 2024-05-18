package com.example.jpashopp.config;

import com.example.jpashopp.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity //스프링 시큐리티 설정을 활성화
@RequiredArgsConstructor
public class SecurityConfig {


    private final MemberService memberService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미한다
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        //permitAll() : 모든 사용자가 인증(로그인)없이 해당 경로를 접근할 수 있도록 설정합니다.
                        .requestMatchers("/", "/members/**", "/item/**", "/assets/**", "/h2-console/**").permitAll()
                        //hasRole(" ") :해당 권한을 가진 사용자만 경로에 접근
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated() //위에 권한을 열어준거 빼고는 전부다 로그인 페이지로 이동

                )

                .formLogin(form -> form //커스텀 로그인 페이지를 설정
                        .loginPage("/members/login")
                        .defaultSuccessUrl("/")
                        .usernameParameter("email")
                        .failureUrl("/members/login/error")
                        .permitAll()
                )

                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                )

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        //인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록
                        //일반 사용자가 관리자 권한 페이지 리소스에 접근시 403(Forbidden) 에러코드를 반환
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
