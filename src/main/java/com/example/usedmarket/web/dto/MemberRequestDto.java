package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotNull(message = "name 필드가 Null 입니다.")
    private String name;

    @Email
    @NotNull(message = "email 필드가 Null 입니다.")
    private String email;

    public Member toMember(){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .role(Role.USER)
                .build();
    }
}
