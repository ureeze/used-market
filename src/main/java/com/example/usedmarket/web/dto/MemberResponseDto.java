package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.repository.member.Member;
import com.example.usedmarket.web.repository.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class MemberResponseDto implements Serializable {
    private Long id;

    private String name;

    private String email;

    private Role role;

    @Builder
    public MemberResponseDto(Long id, String name, String email, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
