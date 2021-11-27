package com.example.usedmarket.web.repository.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum Role {
    GUEST("ROLE_GUEST","게스트"), USER("ROLE_USER", "유저"), ADMIN("ROLE_ADMIN", "관리자");

    private String name;
    private String value;

    Role(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
