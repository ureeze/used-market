package com.example.usedmarket.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    //로그인 사용자 이메일
    private String email;

    //로그인 사용자 비밀번호
    private String password;

}