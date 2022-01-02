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

    //수정하고자 하는 회원 이름
    @NotNull(message = "name 필드가 Null 입니다.")
    private String userName;

}
