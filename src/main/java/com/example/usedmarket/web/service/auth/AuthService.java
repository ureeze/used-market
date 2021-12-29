package com.example.usedmarket.web.service.auth;

import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpDto;

public interface AuthService {
    Long createUser(SignUpDto signUpDto);

    String loginUser(LoginRequestDto loginDto);
}
