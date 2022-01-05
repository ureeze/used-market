package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.PostDetailsResponseDto;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //POST 등록
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> save(@LoginUser UserPrincipal userPrincipal, @Validated @RequestBody PostSaveRequestDto requestDto) {
        PostResponseDto responseDto = postService.save(userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //POST ID로 포스트 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDetailsResponseDto> findById(@PathVariable Long id) {
        PostDetailsResponseDto responseDto = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //POST 제목으로 포스트 목록 조회
    @GetMapping("/posts/all/title")
    public ResponseEntity<List<PostResponseDto>> findByPostTitle(@RequestParam("postTitle") String postTitle) {
        log.info("get Method Call");
        List<PostResponseDto> responseDto = postService.findByPostTitle(postTitle);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //전체 POST 조회
    @GetMapping("/posts/all")
    public ResponseEntity<List<PostResponseDto>> findAll() {
        log.info("get Method Call");
        List<PostResponseDto> responseDtoList = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    //POST 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> update(@PathVariable Long id, @LoginUser UserPrincipal userPrincipal, @Validated @RequestBody PostSaveRequestDto requestDto) {
        log.info("get Method Call");
        PostResponseDto responseDto = postService.updatePost(id, userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //POST 의 ID 을 이용해 POST 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @LoginUser UserPrincipal userPrincipal) {
        log.info("get Method Call");
        postService.delete(id, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
