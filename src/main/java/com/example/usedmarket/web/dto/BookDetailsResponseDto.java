package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.post.Post;
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

    //책 제목
    private String bookTitle;

    //책 재고
    private int stock;

    //책 권당 가격
    private int unitPrice;

    //책 카테고리
    private String bookCategory;

    //책 상태
    private BookStatus bookStatus;

    //책 이미지 주소
    private String bookImgUrl;

    //POST ID
    private Long postId;

    //POST TITLE
    private String postTitle;

    //POST STATUS
    private String postStatus;

    //POST 작성자 ID
    private Long writerId;

    //POST 등록 시간
    private LocalDateTime createdAt;

    public static BookDetailsResponseDto toDto(Book retrieveBook, Post retrievePost) {
        return BookDetailsResponseDto.builder()
                .bookId(retrieveBook.getId())
                .bookTitle(retrieveBook.getTitle())
                .stock(retrieveBook.getStock())
                .unitPrice(retrieveBook.getUnitPrice())
                .bookCategory(retrieveBook.getCategory())
                .bookStatus(retrieveBook.getBookStatus())
                .bookImgUrl(retrieveBook.getImgUrl())
                .postId(retrievePost.getId())
                .postTitle(retrievePost.getTitle())
                .postStatus(retrievePost.getStatus().name())
                .writerId(retrievePost.getUserEntity().getId())
                .createdAt(retrievePost.getCreatedAt())
                .build();
    }
}
