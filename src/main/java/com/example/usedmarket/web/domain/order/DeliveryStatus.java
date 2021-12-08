package com.example.usedmarket.web.domain.order;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    CANCEL_COMPLETED("CANCEL_COMPLETED", "취소 완료"), PAYMENT_COMPLETED("PAYMENT_COMPLETED", "결제완료"), BEING_DELIVERED("BEING_DELIVERED", "배송중"), DELIVERY_COMPLETED("DELIVERY_COMPLETED", "배송완료");

    private String key;
    private String value;

    DeliveryStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
