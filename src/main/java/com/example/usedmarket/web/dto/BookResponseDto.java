package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookStatus;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto implements Serializable {

    //Book ID
    private Long bookId;

    //책 제목
    private String bookTitle;

    //책 재고
    private int stock;

    //책 권당 가격
    private int unitPrice;

    //책 카테고리
    private String bookCategory;

    //책 상태
    private String bookStatus;

    //책 이미지 주소
    private String bookImgUrl;

    public static BookResponseDto toDto(Book retrieveBook){
        return BookResponseDto.builder()
                .bookId(retrieveBook.getId())
                .bookTitle(retrieveBook.getTitle())
                .stock(retrieveBook.getStock())
                .unitPrice(retrieveBook.getUnitPrice())
                .bookCategory(retrieveBook.getCategory())
                .bookStatus(retrieveBook.getBookStatus().name())
                .bookImgUrl(retrieveBook.getImgUrl())
                .build();
    }
}
