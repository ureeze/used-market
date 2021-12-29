package com.example.usedmarket.web.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotNull(message = "name 필드가 Null 입니다.")
    private String name;

    @Email
    @NotNull(message = "email 필드가 Null 입니다.")
    private String email;

}
