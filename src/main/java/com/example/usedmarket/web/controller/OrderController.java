package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.dto.OrderCancelResponseDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderedBookDetailsResponseDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.order.OrderService;
import com.example.usedmarket.web.service.orderedBook.OrderedBookService;
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
    private final OrderedBookService orderedBookService;

    //주문 요청
    @PostMapping("/orders")
    public ResponseEntity<OrderConfirmResponseDto> save(@LoginUser UserPrincipal userPrincipal, @Validated @RequestBody OrderRequestDto requestDto) {
        OrderConfirmResponseDto responseDto = orderService.save(userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //주문 ID 값에 의한 주문 조회
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderConfirmResponseDto> findById(@LoginUser UserPrincipal userPrincipal, @PathVariable Long id) {
        OrderConfirmResponseDto responseDto = orderService.findById(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    //해당 사용자에 대한 주문 전체 조회
    @GetMapping("/orders/all/me")
    public ResponseEntity<List<OrderConfirmResponseDto>> findAll(@LoginUser UserPrincipal userPrincipal) {
        List<OrderConfirmResponseDto> responseDtoList = orderService.findAll(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    //주문 취소
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<OrderCancelResponseDto> cancel(@LoginUser UserPrincipal userPrincipal, @PathVariable Long id) {
        OrderCancelResponseDto orderCancelResponseDto = orderService.cancel(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.OK).body(orderCancelResponseDto);
    }

    //주문한 책 조회
    @GetMapping("/orders/books/{id}")
    public ResponseEntity<OrderedBookDetailsResponseDto> findOrderedBook(@LoginUser UserPrincipal userPrincipal, @PathVariable("id") Long id) {
        OrderedBookDetailsResponseDto responseDto = orderedBookService.findOrderedBook(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //현재 사용자가 주문한 책 목록 조회
    @GetMapping("/orders/books/me")
    public ResponseEntity<List<OrderedBookDetailsResponseDto>> findByCurrentUser(@LoginUser UserPrincipal userPrincipal) {
        List<OrderedBookDetailsResponseDto> responseDtoList = orderedBookService.findByCurrentUser(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

}
