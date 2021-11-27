package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;

import java.util.List;

public interface MemberService {

    //회원등록
    MemberResponseDto createMember(MemberRequestDto dto);

    //회원조회
    MemberResponseDto findById(Long id);

    //회원전체조회
    List<MemberResponseDto> findAll();

    //회원 업데이트
    Long update(Long id, MemberRequestDto requestDto);

    //회원 삭제
    void delete(Long id);

}
