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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final HttpServletResponse response;

    /*
     * 본인정보 조회
     */
    @GetMapping("/users/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@LoginUser UserPrincipal userPrincipal) {
        UserResponseDto findUser = userService.getCurrentUser(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }

    /*
     * 회원 정보 조회 (ADMIN)
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        UserResponseDto findUser = userService.findByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(findUser);
    }

    /*
     * 회원 전체 조회 (ADMIN)
     * */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    /*
     *  본인 정보 수정
     * */
    @PutMapping("/users/me")
    public ResponseEntity<UserResponseDto> updateOne(@LoginUser UserPrincipal userPrincipal, @RequestBody UserUpdateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updatePersonalInfo(userPrincipal, requestDto));
    }

    /*
     *  회원 탈퇴
     * */
    @DeleteMapping("/users/me")
    public HttpStatus delete(@LoginUser UserPrincipal userPrincipal) throws IOException {
        userService.delete(userPrincipal);

        return HttpStatus.OK;
    }
}