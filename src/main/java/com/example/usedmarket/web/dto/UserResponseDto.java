package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponseDto implements Serializable {
    private Long id;

    private String name;

    private String email;


    public static UserResponseDto toDto(UserEntity user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
