package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class IndexController {
//
//    @GetMapping("/")
//    public void base(HttpServletResponse response) throws IOException {
//        response.sendRedirect("http://localhost:3000/list");
//    }

//    @GetMapping("/")
//    public ResponseEntity<LoginResponseDto> base(HttpServletResponse response) throws IOException {
//        response.sendRedirect("http://localhost:3000/");
//        String token = "123123";
//        response.addHeader("Authorization", token);
//        return ResponseEntity.ok(LoginResponseDto.builder().token(token).build());
//    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
