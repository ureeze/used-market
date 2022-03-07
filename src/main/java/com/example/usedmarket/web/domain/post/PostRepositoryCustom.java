package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {

    // POST TITLE 로 PAGING 된 POST 조회
    Page<PostResponseDto> findByPostTitle(Long userId, String postTitle, Pageable pageable);

    // POST 상태가 SELL 인 POST LIST 조회
    List<Post> findByStatusIsSell();

    // 삭제되지 않은 PAGING 된 POST 조회
    Page<PostResponseDto> findByNotDeletedPost(Long userId, Pageable pageable);

    // 현재 사용자의 POST LIST 조회
    List<Post> findByCurrentUser(Long userId);

    // POST ID 로 POST 조회
    Optional<Post> findByPostId(Long postId);
}
