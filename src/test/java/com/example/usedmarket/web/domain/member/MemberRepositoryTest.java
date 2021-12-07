package com.example.usedmarket.web.domain.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    Member createMember() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "PBJ" + num;
        String email = name + "@google.com";

        Member member = Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
        return member;

    }


    @Test
    @DisplayName("controller - 멤버 저장 테스트")
    public void createMemberTest() {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getEmail(), savedMember.getEmail());
    }

    @Test
    @DisplayName("멤버조회 테스트")
    public void findMemberTest() {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        List<Member> list = memberRepository.findAll();
        assertEquals(savedMember.getName(), list.get(0).getName());
        assertEquals(savedMember.getEmail(), list.get(0).getEmail());
    }

    @Test
    @DisplayName("BaseTimeEntity 등록")
    public void baseTimeEntityCreate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        memberRepository.save(createMember());

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        System.out.println("createdAt : " + member.getCreatedAt());
        System.out.println("modifiedAt : " + member.getModifiedAt());
//        assertThat(member.getCreatedAt()).isAfter(now);
//        assertThat(member.getModifiedAt()).isAfter(now);
    }
}