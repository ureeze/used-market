package com.example.usedmarket.web.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("멤버 저장 테스트")
    public void createMemberTest() {
        //given
        String name = "PBJ";
        String email = "PBJ@goole.com";

        Member member = Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertEquals(name, savedMember.getName());
        assertEquals(email, savedMember.getEmail());
        System.out.println(member.toString());
    }

    @Test
    @Transactional
    @DisplayName("멤버조회 테스트")
    public void findMemberTest() {
        //given
        String name = "PBJ";
        String email = "PBJ@goole.com";

        //when
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build());

        //then
        List<Member> list = memberRepository.findAll();
        assertEquals(name, list.get(0).getName());
        assertEquals(email, list.get(0).getEmail());
    }

    @Test
    @DisplayName("BaseTimeEntity 등록")
    public void baseTimeEntityCreate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        memberRepository.save(Member.builder()
                .name("pbj")
                .email("pbj@google.com")
                .picture("pic")
                .role(Role.USER)
                .build());

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        System.out.println("createdAt : " + member.getCreatedAt());
        System.out.println("modifiedAt : " + member.getModifiedAt());
        assertThat(member.getCreatedAt()).isAfter(now);
        assertThat(member.getModifiedAt()).isAfter(now);
    }
}