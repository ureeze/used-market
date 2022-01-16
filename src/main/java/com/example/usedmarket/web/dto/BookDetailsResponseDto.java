package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailsResponseDto implements Serializable {

    //Book ID
    private Long bookId;

    //Book 제목
    private String bookTitle;

    //Book 재고
    private int stock;

    //Book 권당 가격
    private int unitPrice;

    //Book 카테고리
    private String bookCategory;

    //Book 상태
    private String bookStatus;

    //Book 이미지 주소
    private String bookImgUrl;

    //Book 등록 일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //POST ID
    private Long postId;

    public static BookDetailsResponseDto toDto(Book retrieveBook) {
        return BookDetailsResponseDto.builder()
                .bookId(retrieveBook.getId())
                .bookTitle(retrieveBook.getTitle())
                .stock(retrieveBook.getStock())
                .unitPrice(retrieveBook.getUnitPrice())
                .bookCategory(retrieveBook.getCategory())
                .bookStatus(retrieveBook.getBookStatus().name())
                .bookImgUrl(retrieveBook.getImgUrl())
                .createdAt(retrieveBook.getCreatedAt())
                .postId(retrieveBook.getPost().getId())
                .build();
    }
}
