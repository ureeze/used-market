package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.querydsl.core.Tuple;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static com.example.usedmarket.web.domain.book.QBook.book;
import static com.example.usedmarket.web.domain.orderedBook.QOrderedBook.orderedBook;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderedBookDetailsResponseDto implements Serializable {
    //ORDERED BOOK ID
    private Long id;

    //ORDERED BOOK 수량
    private int amount;

    //ORDERED BOOK 가격
    private int orderPrice;

    //ORDERED BOOK 제목
    private String bookTitle;

    //ORDERED BOOK 분류
    private String bookCategory;

    //ORDERED BOOK 상태
    private String bookStatus;

    public static OrderedBookDetailsResponseDto toResponseDto(OrderedBook orderedBook, Book book) {
        return OrderedBookDetailsResponseDto.builder()
                .id(orderedBook.getId())
                .amount(orderedBook.getAmount())
                .orderPrice(orderedBook.getOrderPrice())
                .bookTitle(book.getTitle())
                .bookCategory(book.getCategory())
                .bookStatus(book.getBookStatus().name())
                .build();
    }

}
