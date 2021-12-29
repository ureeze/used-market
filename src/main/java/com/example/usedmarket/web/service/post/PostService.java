package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.dto.PostSaveResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface PostService {

    //포스트 등록
    PostSaveResponseDto save(UserPrincipal userPrincipal, PostSaveRequestDto requestDTO);

    //포스트 조회
    PostSaveResponseDto findById(Long id);

    //포스트 전체 조회
    List<PostSaveResponseDto> findAll();

    //포스트 수정
    PostSaveResponseDto update(Long id, PostSaveRequestDto requestDTO);

    //포스트 삭제
    void delete(Long id);
}
