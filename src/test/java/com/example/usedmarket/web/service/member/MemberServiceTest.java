package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.dto.MemberResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

     MemberRequestDto createRequestDto() {
        int num = (int) (Math.random() * 10000) + 1;

        String name = "PBJ" + num;
        String email = name + "@google.com";
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name(name)
                .email(email)
                .build();
        return requestDto;
    }


    @AfterEach
     void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("service - 멤버 생성 테스트")
    void saveMemberTest() {
        //given
        MemberRequestDto dto = createRequestDto();

        //when
        MemberResponseDto responseDto = memberService.createMember(dto);

        //then
        assertEquals(dto.getName(), responseDto.getName());
        assertEquals(dto.getEmail(), responseDto.getEmail());
    }

    @Test
    @DisplayName("service - 멤버 조회 테스트")
    void findMemberTest() {
        //given
        MemberRequestDto requestDto = createRequestDto();
        Member savedMember = memberRepository.save(requestDto.toMember());

        //when
        MemberResponseDto savedMemberDto = memberService.findById(savedMember.getId());

        //then
        assertEquals(requestDto.getName(), savedMemberDto.getName());
        assertEquals(requestDto.getEmail(), savedMemberDto.getEmail());
    }

    @Test
    @DisplayName("service - 멤버 전체조회 테스트")
    void findAllMemberTest() {
        //given
        Member member0 = createRequestDto().toMember();
        Member savedMember0 = memberRepository.save(member0);

        Member member1 = createRequestDto().toMember();
        Member savedMember1 = memberRepository.save(member1);

        //when
        List<MemberResponseDto> savedMemberDto = memberService.findAll();

        //then
        assertEquals(member0.getName(), savedMemberDto.get(0).getName());
        assertEquals(member1.getName(), savedMemberDto.get(1).getName());
    }
}