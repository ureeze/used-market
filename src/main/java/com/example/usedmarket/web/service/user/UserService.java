package com.example.usedmarket.web.service.user;

import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface UserService {
    //본인 정보 조회
    UserResponseDto getCurrentUser(UserPrincipal userPrincipal);

    //USER ID 를 이용한 사용자 조회 (ADMIN)
    UserResponseDto findByUserId(Long userId);

    //전체 USER 목록 조회 (ADMIN)
    List<UserResponseDto> findAll();

    //사용자 정보 수정
    UserResponseDto updatePersonalInfo(UserPrincipal userPrincipal, UserUpdateRequestDto requestDto);

    //회원 탈퇴
    void delete(UserPrincipal userPrincipal) throws IOException;
}
