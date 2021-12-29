package com.example.usedmarket.web.service.user;

import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;

import java.util.List;

public interface UserService {

    UserResponseDto findById(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto update(Long id, UserUpdateRequestDto requestDto);

    void delete(Long id);
}
