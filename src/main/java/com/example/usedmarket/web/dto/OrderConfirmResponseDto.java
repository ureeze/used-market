package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Builder
public class OrderConfirmResponseDto implements Serializable {

    //주문 ID
    private Long orderId;

    //받는 사람
    private String recipient;

    //주소
    private String address;

    //전화번호
    private String phone;

    //주문 상태
    private String deliveryStatus;

    //총 구매 권 수
    private int bookAmount;

    //총 결제 금액
    private int orderPrice;

    //카테고리(BOOK)
    private String bookCategory;

    //책 상태(BOOK)
    private String bookStatus;

    //책 제목(BOOK)
    private String bookTitle;

    //주문된 책 ID
    private Long orderedBookId;

    //주문 일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //Book 이미지 주소
    private String bookImgUrl;

    //주문 POST ID
    private Long postId;

    public static OrderConfirmResponseDto toDto(Order order, OrderedBook orderedBook) {
        return OrderConfirmResponseDto.builder()
                .orderId(order.getId())
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .phone(order.getPhone())
                .bookCategory(orderedBook.getBook().getCategory())
                .bookAmount(orderedBook.getAmount())
                .orderPrice(orderedBook.getOrderPrice())
                .bookStatus(orderedBook.getBook().getBookStatus().name())
                .deliveryStatus(order.getDeliveryStatus().getValue())
                .bookTitle(orderedBook.getBook().getTitle())
                .orderedBookId(orderedBook.getId())
                .createdAt(order.getCreatedAt())
                .bookImgUrl(orderedBook.getBook().getImgUrl())
                .postId(order.getPost().getId())
                .build();
    }
}
