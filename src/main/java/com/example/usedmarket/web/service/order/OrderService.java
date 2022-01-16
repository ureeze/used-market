package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.dto.OrderCancelResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface OrderService {

    //주문 진행
    OrderConfirmResponseDto save(UserPrincipal userPrincipal, OrderRequestDto requestDto);

    //주문 ID 값에 의한 주문 조회
    OrderConfirmResponseDto findById(UserPrincipal userPrincipal,Long id);

    //해당 사용자에 대한 주문 전체 조회
    List<OrderConfirmResponseDto> findAll(UserPrincipal userPrincipal);

    //주문 취소
    OrderCancelResponseDto cancel(UserPrincipal userPrincipal, Long orderId);

}
