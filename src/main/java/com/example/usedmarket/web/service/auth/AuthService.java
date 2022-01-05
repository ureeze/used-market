package com.example.usedmarket.web.service.auth;

import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpRequestDto;

public interface AuthService {
    //회원 가입
    Long createUser(SignUpRequestDto signUpDto);

    //로그인
    String loginUser(LoginRequestDto loginDto);
}
