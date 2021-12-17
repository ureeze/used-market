package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveResponseDto implements Serializable {

    private Long postId;
    private String title;
    private String content;
    private PostStatus status;
    private LocalDateTime createdAt;
    private Long memberId;
    @JsonIgnore
    private List<Book> bookList;

    public static PostSaveResponseDto toResponseDto(Post post) {
        return PostSaveResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .memberId(post.getMember().getId())
                .bookList(post.getBookList())
                .build();
    }
}