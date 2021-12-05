package com.example.usedmarket.web.domain.order;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    BEING_DELIVERED("BEING_DELIVERED", "배송중"), DELIVERY_COMPLETED("DELIVERY_COMPLETED", "배송완료");

    private String key;
    private String value;

    DeliveryStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
