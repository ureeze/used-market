package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class OrderRequestDto {
    //받는 사람
    @NotNull
    private String recipient;

    //주소
    @NotNull
    private String address;

    //전화번호
    @NotNull
    private String phone;

    //책 권수
    @NotNull
    private int count;

    //주문 가격
    @NotNull
    private int orderPrice;

    //포스트 ID
    @NotNull
    private Long postId;

    //책 ID
    @NotNull
    private Long bookId;

    public Order createOrder(UserEntity user, Post post) {
        Order order = Order.builder()
                .recipient(this.recipient)
                .address(this.address)
                .phone(this.phone)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .user(user)
                .post(post)
                .build();
        return order;
    }

    public OrderedBook createOrderedBook(Order order, Book book) {
        OrderedBook orderedBook = OrderedBook.builder()
                .count(this.count)
                .orderPrice(this.orderPrice)
                .order(order)
                .book(book)
                .build();

        //Order 와 OrderedBook 의 연관관계 지정
        order.getOrderedBookList().add(orderedBook);
        return orderedBook;
    }
}