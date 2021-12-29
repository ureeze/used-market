package com.example.usedmarket.web.domain.user;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER", "유저"), ADMIN("ROLE_ADMIN", "관리자");

    private String key;
    private String value;

    Role(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
