package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto implements Serializable {

    // TOKEN
    String token;

    // USER ID
    Long userId;

    // USER EMAIL
    String email;

    // USER NAME
    String name;

    public static LoginResponseDto toDto(String token, UserPrincipal userPrincipal){
        return LoginResponseDto.builder()
                .token(token)
                .userId(userPrincipal.getId())
                .email(userPrincipal.getEmail())
                .name(userPrincipal.getName())
                .build();
    }
}
