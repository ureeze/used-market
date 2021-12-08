package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.dto.OrderCancelResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderResponseDto;
import com.example.usedmarket.web.security.LoginUser;
import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문 진행
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> save(@LoginUser SessionMember sessionMember, @Validated @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto responseDto = orderService.save(sessionMember, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //주문 조회
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable Long id) {
        OrderResponseDto responseDto = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //ID 값에 의한 주문 전체조회
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> findAll(@LoginUser SessionMember sessionMember) {
        List<OrderResponseDto> responseDtoList = orderService.findAll(sessionMember);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    //주문 취소
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderCancelResponseDto> cancel(@LoginUser SessionMember sessionMember, @PathVariable Long id) {
        orderService.cancel(sessionMember, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(OrderCancelResponseDto.builder()
                .cancelOrderId(id)
                .deliveryStatus(DeliveryStatus.CANCEL_COMPLETED.name())
                .build());
    }
}