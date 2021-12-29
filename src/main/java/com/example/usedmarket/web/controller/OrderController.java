package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.dto.OrderCancelResponseDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.order.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    //주문 진행
    @PostMapping("/orders")
    public ResponseEntity<OrderConfirmResponseDto> save(@LoginUser UserPrincipal userPrincipal, @Validated @RequestBody OrderRequestDto requestDto) {
        OrderConfirmResponseDto responseDto = orderService.save(userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //주문 조회
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderConfirmResponseDto> findById(@PathVariable Long id) {
        OrderConfirmResponseDto responseDto = orderService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //ID 값에 의한 주문 전체조회
    @GetMapping("/orders")
    public ResponseEntity<List<OrderConfirmResponseDto>> findAll(@LoginUser UserPrincipal userPrincipal) {
        List<OrderConfirmResponseDto> responseDtoList = orderService.findAll(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    //주문 취소
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderCancelResponseDto> cancel(@LoginUser UserPrincipal userPrincipal, @PathVariable Long id) {
        orderService.cancel(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(OrderCancelResponseDto.builder()
                .cancelOrderId(id)
                .deliveryStatus(DeliveryStatus.CANCEL_COMPLETED.name())
                .build());
    }
}
