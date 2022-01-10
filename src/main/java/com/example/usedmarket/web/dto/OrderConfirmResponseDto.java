package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
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
    private String bookName;

    //주문된 책 ID
    private Long orderedBookId;

    //주문된 책 리스트
    private List<OrderedBookDetailsResponseDto> responseDtoList = new ArrayList<>();

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
                .deliveryStatus(order.getDeliveryStatus().name())
                .bookName(orderedBook.getBook().getTitle())
                .orderedBookId(orderedBook.getId())
                .build();
    }

    public static OrderConfirmResponseDto responseDto(Order order, List<OrderedBookDetailsResponseDto> responseDtoList) {
        int bookAmount = 0;
        int orderPrice = 0;
        for (OrderedBookDetailsResponseDto dto : responseDtoList) {
            bookAmount += dto.getAmount();
            orderPrice += dto.getOrderPrice();
        }
        return OrderConfirmResponseDto.builder()
                .orderId(order.getId())
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .phone(order.getPhone())
                .deliveryStatus(order.getDeliveryStatus().name())
                .bookAmount(bookAmount)
                .orderPrice(orderPrice)
                .responseDtoList(responseDtoList)
                .build();
    }
}
