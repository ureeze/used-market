package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Builder
public class OrderConfirmResponseDto implements Serializable {
    //받는 사람
    private String recipient;

    //주소
    private String address;

    //전화번호
    private String phone;

    //카테고리
    private String bookCategory;

    //구매 권 수
    private int bookAmount;

    //결제 금액
    private int orderPrice;

    //책 상태
    private String bookStatus;

    //책 제목
    private String bookName;

    //주문 상태
    private String deliveryStatus;

    //주문 ID
    private Long orderId;

    //주문된 책 ID
    private Long orderedBookId;



    public static OrderConfirmResponseDto toDto(Order order, OrderedBook orderedBook) {
        return OrderConfirmResponseDto.builder()
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .phone(order.getPhone())
                .bookCategory(orderedBook.getBook().getCategory())
                .bookAmount(orderedBook.getAmount())
                .orderPrice(orderedBook.getOrderPrice())
                .bookStatus(orderedBook.getBook().getBookStatus().name())
                .deliveryStatus(order.getDeliveryStatus().name())
                .bookName(orderedBook.getBook().getTitle())
                .orderId(order.getId())
                .orderedBookId(orderedBook.getId())
                .build();
    }
}
