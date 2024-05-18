package com.example.jpashopp.domain.members;

import com.example.jpashopp.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.crypto.password.PasswordEncoder;


@NoArgsConstructor
@Getter
@Setter
@Table(name = "member")
@Entity
public class Member {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;



    /*@Builder 패턴을 사용하여 전체 생성자를 생성하고, 어디서든지 그 생성자를 쉽게 사용할 수 있도록
    Builder 패턴을 적용한 것입니다. 이렇게 하면, 클래스 외부에서 객체를 생성할 때 더 유연하고 가독성 있게 사용할 수 있습니다.*/

    @Builder
    public Member(String name, String email, String password, String address, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))  //암호화처리
                .role(MemberRole.USER)
                .build();
        return member;
    }


}