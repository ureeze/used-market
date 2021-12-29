package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface OrderService {

    //주문 진행
    OrderConfirmResponseDto save(UserPrincipal userPrincipal, OrderRequestDto requestDto);

    //주문 조회
    OrderConfirmResponseDto findById(Long id);

    //세션에 의한 주문 전체조회
    List<OrderConfirmResponseDto> findAll(UserPrincipal userPrincipal);

    //주문 취소
    void cancel(UserPrincipal userPrincipal, Long orderId);

}
