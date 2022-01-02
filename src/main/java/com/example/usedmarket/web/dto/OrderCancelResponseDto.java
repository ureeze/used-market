package com.example.usedmarket.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class OrderCancelResponseDto implements Serializable {

    //취소된 주문 ID
    private Long cancelOrderId;

    //주문 상태
    private String deliveryStatus;

}