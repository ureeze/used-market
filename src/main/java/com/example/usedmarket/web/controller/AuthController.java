package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpDto;
import com.example.usedmarket.web.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Long> signup(@RequestBody SignUpDto signUpDto) {
        Long id = authService.createUser(signUpDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginDto) {
        String token = authService.loginUser(loginDto);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
