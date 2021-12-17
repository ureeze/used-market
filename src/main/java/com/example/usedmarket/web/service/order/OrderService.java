package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.security.dto.SessionMember;

import java.util.List;

public interface OrderService {

    //주문 진행
    OrderConfirmResponseDto save(SessionMember sessionMember, OrderRequestDto requestDto);

    //주문 조회
    OrderConfirmResponseDto findById(Long id);

    //세션에 의한 주문 전체조회
    List<OrderConfirmResponseDto> findAll(SessionMember sessionMember);

    //주문 취소
    void cancel(SessionMember sessionMember, Long orderId);

}
