package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponseDto {
    //받는 사람
    private String recipient;

    //주소
    private String address;

    //전화번호
    private String phone;

    //카테고리
    private String category;

    //구매 권 수
    private int count;

    //결제금액
    private int orderPrice;

    //책 상태
    private String bookStatus;

    //책 제목
    private String bookName;

    //주문 ID
    private Long orderId;

    //주문된 책 ID
    private Long orderedBookId;

    public static OrderResponseDto toDto(Order order, OrderedBook orderedBook) {
        return OrderResponseDto.builder()
                .recipient(order.getRecipient())
                .address(order.getAddress())
                .phone(order.getPhone())
                .category(orderedBook.getBook().getCategory())
                .count(orderedBook.getCount())
                .orderPrice(orderedBook.getOrderPrice())
                .bookStatus(orderedBook.getBook().getBookStatus().name())
                .bookName(orderedBook.getBook().getBookName())
                .orderId(order.getId())
                .orderedBookId(orderedBook.getId())
                .build();
    }
}
