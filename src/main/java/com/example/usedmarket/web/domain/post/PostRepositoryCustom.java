package com.example.usedmarket.web.domain.post;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    List<Post> findByPostTitle(String postTitle);

    List<Post> findByStatusIsSell();
    List<Post> findByPostIsNotDeleted();
    List<Post> findByAllPostAboutMyself(Long userId);

    Optional<Post> findByPostId(Long postId);
}
