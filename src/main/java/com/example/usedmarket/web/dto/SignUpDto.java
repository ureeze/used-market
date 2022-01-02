package com.example.usedmarket.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    //회원 가입자 이름
    private String userName;

    //회원 가입자 EMAIL
    private String email;

    //회원 가입자 비밀번호
    private String password;

}
