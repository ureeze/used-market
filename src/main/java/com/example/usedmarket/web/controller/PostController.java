package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.config.auth.LoginUser;
import com.example.usedmarket.web.config.auth.dto.SessionMember;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> save(@LoginUser SessionMember member, PostSaveRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
