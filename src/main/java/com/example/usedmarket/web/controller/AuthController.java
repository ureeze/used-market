package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.LoginResponseDto;
import com.example.usedmarket.web.dto.SignUpRequestDto;
import com.example.usedmarket.web.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입
    @PostMapping("/auth/signup")
    public ResponseEntity<Long> signup(@RequestBody SignUpRequestDto signUpDto) {
        log.info("SIGN UP");
        Long userId = authService.createUser(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    // 로그인 (Email + Password)
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        log.info("LOG IN");
        LoginResponseDto loginResponseDto = authService.loginUser(loginDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponseDto);
    }

    @GetMapping("/login/code")
    public void response(){
        log.info("confirm");
    }
}
