package com.example.usedmarket.web.domain.post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findByPostTitle(String postTitle);

    List<Post> findByStatusIsSell();
    List<Post> findByPostIsNotDeleted();
    List<Post> findByAllPostAboutMyself(Long userId);
}
