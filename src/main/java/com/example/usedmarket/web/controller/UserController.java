package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @PutMapping("/users")
    public ResponseEntity<UserResponseDto> updateOne(@LoginUser UserPrincipal userPrincipal, @Validated @RequestBody UserUpdateRequestDto responseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePersonalInfo(userPrincipal, responseDto));
    }

    /*
     *  회원 탈퇴
     * */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> delete(@LoginUser UserPrincipal userPrincipal, @PathVariable Long id) throws IOException {
        userService.delete(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseDto.builder().id(id).build());
    }
}