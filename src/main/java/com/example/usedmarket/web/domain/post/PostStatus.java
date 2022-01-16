package com.example.usedmarket.web.domain.post;

import lombok.Getter;

@Getter
public enum PostStatus {
    SELL("SELL", "판매중"), SOLD_OUT("SOLD_OUT", "판매완료"), DELETED("DELETED","삭제완료");

    private String key;
    private String value;

    PostStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
