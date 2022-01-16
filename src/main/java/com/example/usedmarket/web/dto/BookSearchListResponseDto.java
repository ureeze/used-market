package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookSearchListResponseDto implements Serializable {

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

    //POST ID
    private Long postId;

    //POST TITLE
    private String postTitle;

    //POST STATUS
    private String postStatus;

    //POST 작성자 ID
    private Long writerId;

    //POST 등록 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static BookSearchListResponseDto toDto(Book retrieveBook, Post retrievePost) {
        return BookSearchListResponseDto.builder()
                .bookId(retrieveBook.getId())
                .bookTitle(retrieveBook.getTitle())
                .stock(retrieveBook.getStock())
                .unitPrice(retrieveBook.getUnitPrice())
                .bookCategory(retrieveBook.getCategory())
                .bookStatus(retrieveBook.getBookStatus().name())
                .bookImgUrl(retrieveBook.getImgUrl())
                .postId(retrievePost.getId())
                .postTitle(retrievePost.getTitle())
                .postStatus(retrievePost.getStatus().name())
                .writerId(retrievePost.getUserEntity().getId())
                .createdAt(retrievePost.getCreatedAt())
                .build();
    }
}
