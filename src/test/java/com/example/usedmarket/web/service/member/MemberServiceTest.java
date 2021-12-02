package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @DisplayName("service - 멤버 생성 테스트")
    public void saveMemberTest() {
        //given
        MemberRequestDto dto = createMember();

        //when
        MemberResponseDto responseDto = memberService.createMember(dto);

        //then
        assertEquals(dto.getName(), responseDto.getName());
        assertEquals(dto.getEmail(), responseDto.getEmail());
    }

    @Test
    @DisplayName("service - 멤버 조회 테스트")
    public void findMemberTest() {
        //given
        MemberRequestDto dto = createMember();
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(Role.USER)
                .picture("pic")
                .build();
        Member savedMember = memberRepository.save(member);

        //when
        MemberResponseDto savedMemberDto = memberService.findById(savedMember.getId());

        //then
        assertEquals(dto.getName(), savedMemberDto.getName());
        assertEquals(dto.getEmail(), savedMemberDto.getEmail());
    }

    @Test
    @DisplayName("service - 멤버 전체조회 테스트")
    public void findAllMemberTest() {
        //given
        Member member0 = Member.builder()
                .name("pbj0")
                .email("pbj0@google.com")
                .role(Role.USER)
                .picture("pic")
                .build();
        Member savedMember0 = memberRepository.save(member0);
        Member member1 = Member.builder()
                .name("pbj1")
                .email("pbj1@google.com")
                .role(Role.USER)
                .picture("pic")
                .build();
        Member savedMember1 = memberRepository.save(member1);

        //when
        List<MemberResponseDto> savedMemberDto = memberService.findAll();

        //then
        assertEquals(member0.getName(), savedMemberDto.get(0).getName());
        assertEquals(member1.getName(), savedMemberDto.get(1).getName());
    }
}