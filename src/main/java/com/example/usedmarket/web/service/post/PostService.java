package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    //POST 등록
    PostResponseDto save(UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) throws ParseException;

    //POST ID로 POST 상세 조회
    PostResponseDto findById(UserPrincipal userPrincipal, Long postId);

    //POST 제목으로 POST 조회
    Page<PostResponseDto> findByPostTitle(UserPrincipal userPrincipal, String postTitle, Pageable pageable);

    //전체 POST 조회
    Page<PostResponseDto> findAll(UserPrincipal userPrincipal, Pageable pageable);

    //자신의 전체 POST 조회
    List<PostResponseDto> findByAllPostAboutMyself(UserPrincipal userPrincipal);

    //POST 수정
    PostResponseDto updatePost(Long postId, UserPrincipal userPrincipal, PostSaveRequestDto requestDTO);

    //POST 의 ID 을 이용해 POST 삭제
    void delete(Long postId, UserPrincipal userPrincipal);
}
