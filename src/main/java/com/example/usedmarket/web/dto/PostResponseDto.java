package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto implements Serializable {

    //POST ID
    private Long postId;

    //POST TITLE
    private String postTitle;

    //POST STATUS
    private String postStatus;

    //POST CONTENT
    private String postContent;

    //POST 등록시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    //POST 작성자 ID
    private Long writerId;

    //POST 작성자 이름
    private String writerName;

    //함께 등록한 BOOK
    private BookDetailsResponseDto book;

    //현재 로그인 된 USER ID
    private Long authenticationId;

//    //Book 제목
//    private String bookTitle;
//
//    //Book 재고수량
//    private int stock;
//
//    //Book Image Url
//    private String imgUrl;

    public static PostResponseDto toResponseDto(Long authenticationId, Post post) {
//        StringBuilder content = new StringBuilder();
//        content.append(post.getContent());
//        if (content.length() > 20) {
//            content.substring(0, 20);
//            content.append("...");
//        }
        return PostResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postStatus(post.getStatus().name())
                .postContent(post.getContent())
                .createdAt(post.getCreatedAt())
                .writerId(post.getUserEntity().getId())
                .writerName(post.getUserEntity().getName())
                .book(BookDetailsResponseDto.toDto(post.getBookList().get(0)))
                .authenticationId(authenticationId)
                .build();
//                .bookTitle(post.getBookList().get(0).getTitle())
//                .stock(post.getBookList().get(0).getStock())
//                .imgUrl(post.getBookList().get(0).getImgUrl())
    }
}