package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private String postStatus;

    //POST 등록시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    //POST 작성자 ID
    private Long writerId;

    //POST 와 함께 등록된 책
    @JsonIgnore
    private List<BookDetailsResponseDto> bookList;


    public static PostDetailsResponseDto toResponseDto(Post post) {
        List<BookDetailsResponseDto> bookDetailsDto = post.getBookList()
                .stream()
                .map(book -> BookDetailsResponseDto.toDto(book))
                .collect(Collectors.toList());
        return PostDetailsResponseDto.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .postStatus(post.getStatus().name())
                .createdAt(post.getCreatedAt())
                .writerId(post.getUserEntity().getId())
                .bookList(bookDetailsDto)
                .build();
    }
}