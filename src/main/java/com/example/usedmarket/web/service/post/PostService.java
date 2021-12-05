package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;

import java.util.List;

public interface PostService {

    //포스트 등록
    PostResponseDto save(SessionMember member, PostSaveRequestDto requestDTO);

    //포스트 조회
    PostResponseDto findById(Long id);

    //포스트 전체 조회
    List<PostResponseDto> findAll();

    //포스트 수정
    PostResponseDto update(Long id, PostSaveRequestDto requestDTO);

    //포스트 삭제
    void delete(Long id);
}
