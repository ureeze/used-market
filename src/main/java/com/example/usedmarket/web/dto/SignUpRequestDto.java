package com.example.usedmarket.web.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto implements Serializable {

    //회원 가입자 EMAIL
    @NotNull
    @Email
    private String email;

    //회원 가입자 이름
    @NotNull
    @Size(min = 4)
    private String userName;

    //회원 가입자 비밀번호
    @NotNull
    @Size(min = 4)
    private String password;

}
