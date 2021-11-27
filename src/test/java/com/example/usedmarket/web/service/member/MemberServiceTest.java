package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.repository.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    public MemberRequestDto createMember() {
        String name = "PBJ";
        String email = "PBJ@google.com";
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name(name)
                .email(email)
                .build();
        return requestDto;
    }

    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        MemberRequestDto dto = createMember();
        MemberResponseDto responseDto = memberService.createMember(dto);

        assertEquals(dto.getName(), responseDto.getName());
        assertEquals(dto.getEmail(), responseDto.getEmail());
    }

    @Test
    @DisplayName("회원조회 테스트")
    public void findMemberTest() {
        MemberRequestDto dto = createMember();
        MemberResponseDto savedMemberDto = memberService.createMember(dto);
        assertEquals(dto.getName(), memberRepository.findAll().get(0).getName());
    }
}