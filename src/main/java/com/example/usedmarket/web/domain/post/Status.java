package com.example.usedmarket.web.domain.post;

import lombok.Getter;

@Getter

public enum Status {
    SELL("SELL", "판매중"), SOLD_OUT("SOLD_OUT", "판매완료"), PAUSE("PAUSE","보류");

    private String key;
    private String value;

    Status(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
