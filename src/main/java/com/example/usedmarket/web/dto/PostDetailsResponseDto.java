package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailsResponseDto implements Serializable {

    //POST ID
    private Long postId;

    //POST TITLE
    private String postTitle;

    //POST CONTENT
    private String postContent;

    //POST STATUS
    private PostStatus postStatus;

    //POST 등록시간
    private LocalDateTime createdAt;

    //POST 작성자 ID
    private Long writerId;

    //POST 와 함께 등록된 책
    @JsonIgnore
    private List<Book> bookList;


    public static PostDetailsResponseDto toResponseDto(Post post) {
        return PostDetailsResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .postStatus(post.getStatus())
                .createdAt(post.getCreatedAt())
                .writerId(post.getUserEntity().getId())
                .bookList(post.getBookList())
                .build();
    }
}