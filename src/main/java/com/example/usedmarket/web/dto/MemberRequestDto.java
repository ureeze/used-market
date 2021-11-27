package com.example.usedmarket.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {

    private String name;
    private String email;

    @Builder
    public MemberRequestDto(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
