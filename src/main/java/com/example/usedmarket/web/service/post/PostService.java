package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.dto.PostDetailsResponseDto;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface PostService {

    //POST 등록
    PostResponseDto save(UserPrincipal userPrincipal, PostSaveRequestDto requestDTO);

    //POST ID로 포스트 조회
    PostDetailsResponseDto findById(Long postId);

    //POST 제목으로 포스트 조회
    List<PostResponseDto> findByPostTitle(String postTitle);

    //전체 POST 조회
    List<PostResponseDto> findAll();

    //POST 수정
    PostResponseDto updatePost(Long postId, UserPrincipal userPrincipal, PostSaveRequestDto requestDTO);

    //POST 의 ID 을 이용해 POST 삭제
    void delete(Long postId, UserPrincipal userPrincipal);
}
