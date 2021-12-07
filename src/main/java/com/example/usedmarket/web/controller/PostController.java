package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.LoginUser;
import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //포스트 저장
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> save(@LoginUser SessionMember member, @Validated @RequestBody PostSaveRequestDto requestDto) {
        PostResponseDto responseDto = postService.save(member, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    //포스트 조회
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable Long id) {
        PostResponseDto responseDto = postService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //포스트 전체조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> findAll() {
        List<PostResponseDto> responseDtoList = postService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    //포스트 수정
    @PutMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> update(@PathVariable Long id, @Validated @RequestBody PostSaveRequestDto requestDto) {
        PostResponseDto responseDto = postService.update(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //포스트 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<PostResponseDto> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(PostResponseDto.builder().postId(id).build());
    }

}
