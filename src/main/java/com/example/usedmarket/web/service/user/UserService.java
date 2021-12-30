package com.example.usedmarket.web.service.user;

import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserResponseDto findById(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto updatePersonalInfo(UserPrincipal userPrincipal, UserUpdateRequestDto requestDto);

    void delete(UserPrincipal userPrincipal, Long id) throws IOException;
}
