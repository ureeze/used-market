package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
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
public class PostResponseDto implements Serializable {

    //POST ID
    private Long postId;

    //POST TITLE
    private String postTitle;

    //POST STATUS
    private PostStatus postStatus;

    //POST 등록시간
    private LocalDateTime createdAt;

    //POST 작성자 ID
    private Long writerId;

    public static PostResponseDto toResponseDto(Post post) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postStatus(post.getStatus())
                .createdAt(post.getCreatedAt())
                .writerId(post.getUserEntity().getId())
                .build();
    }
}