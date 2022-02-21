package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Page<PostResponseDto> findByPostTitle(Long userId, String postTitle, Pageable pageable);

    List<Post> findByStatusIsSell();

    Page<PostResponseDto> findByPostIsNotDeleted(Long userId, Pageable pageable);

    List<Post> findByAllPostAboutMyself(Long userId);

    Optional<Post> findByPostId(Long postId);
}
