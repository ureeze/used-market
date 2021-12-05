package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String name;
    private String email;

    public Member toMember(){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .role(Role.USER)
                .build();
    }
}
