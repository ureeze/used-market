package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.*;

import java.io.Serializable;
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponseDto implements Serializable {
    //회원 ID
    private Long userId;

    //회원 이름
    private String userName;

    //회원 EMAIL
    private String email;


    public static UserResponseDto toDto(UserEntity user){
        return UserResponseDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .build();
    }
}
