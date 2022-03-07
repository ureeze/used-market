package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // POST 등록
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> save(@LoginUser UserPrincipal userPrincipal, @Validated @RequestBody PostSaveRequestDto requestDto) throws ParseException {
        log.info("POST 등록");
        PostResponseDto responseDto = postService.save(userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // POST ID로 포스트 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> findById(@LoginUser UserPrincipal userPrincipal, @PathVariable Long id) {
        log.info("POST ID로 포스트 조회");
        PostResponseDto responseDto = postService.findById(userPrincipal, id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // POST 제목으로 포스트 목록 조회
    @GetMapping("/posts/all/title")
    public ResponseEntity<Page<PostResponseDto>> findByPostTitle(@LoginUser UserPrincipal userPrincipal, @RequestParam("title") String postTitle, Pageable pageable) {
        log.info("POST 제목으로 포스트 목록 조회");
        Page<PostResponseDto> responseDto = postService.findByPostTitle(userPrincipal, postTitle, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 전체 POST 조회
    @GetMapping("/posts/all")
    public ResponseEntity<Page<PostResponseDto>> findAll(@LoginUser UserPrincipal userPrincipal, Pageable pageable) {
        log.info("전체 POST 조회");
        Page<PostResponseDto> responseDtoList = postService.findAll(userPrincipal, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 현재 사용자가 판매 중인 POST 조회
    @GetMapping("/posts/me")
    public ResponseEntity<List<PostResponseDto>> findByAllPostAboutMyself(@LoginUser UserPrincipal userPrincipal) {
        log.info("현재 사용자가 판매 중인 POST 조회");
        List<PostResponseDto> responseDtoList = postService.findByAllPostAboutMyself(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // POST 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> update(@PathVariable Long id, @LoginUser UserPrincipal userPrincipal, @Validated @RequestBody PostSaveRequestDto requestDto) {
        log.info("POST 수정");
        PostResponseDto responseDto = postService.updatePost(id, userPrincipal, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // POST 의 ID 을 이용해 POST 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @LoginUser UserPrincipal userPrincipal) {
        log.info("POST 의 ID 을 이용해 POST 삭제");
        postService.delete(id, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }
}
