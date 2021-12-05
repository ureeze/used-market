package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto implements Serializable {

    private Long postId;
    private String title;
    private String content;
    private PostStatus status;
    private LocalDateTime createdAt;
    private Long memberId;
    private List<Book> bookList;


    public static PostResponseDto toResponseDto(Post post) {
        return PostResponseDto.builder()
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