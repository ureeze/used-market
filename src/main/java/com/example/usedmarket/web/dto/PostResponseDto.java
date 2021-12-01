package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto implements Serializable {

    private Long postId;
    private String title;
    private String content;
    private Status status;
    private LocalDateTime createdAt;
    private Long memberId;

    public static PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .status(post.getStatus())
                .createdAt(post.getCreateAt())
                .memberId(post.getMember().getId())
                .build();
    }
}