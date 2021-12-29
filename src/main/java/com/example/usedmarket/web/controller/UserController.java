package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String base() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "base";
    }

    @GetMapping("/home")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "home";
    }

    /*
     * 회원정보 조회
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto findUser = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }


    /*
     * 회원 전체 조회
     * */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }


    /*
     *  회원정보 수정
     * */
    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateOne(@PathVariable Long id, @Validated @RequestBody UserUpdateRequestDto responseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, responseDto));
    }

    /*
     *  회원 탈퇴
     * */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseDto.builder().id(id).build());
    }
}