package com.example.usedmarket.web.repository.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("멤버 저장 테스트")
    public void createMemberTest() {
        //given
        String name = "PBJ";
        String email = "PBJ@goole.com";

        Member member = Member.builder()
                .name(name)
                .email(email)
                .role(Role.GUEST)
                .createAt(LocalDateTime.now())
                .build();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        assertEquals(name, savedMember.getName());
        assertEquals(email, savedMember.getEmail());
        System.out.println(member.toString());
    }

    @Test
    @DisplayName("멤버조회 테스트")
    public void findMemberTest() {
        //given
        String name = "PBJ";
        String email = "PBJ@goole.com";

        //when
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .createAt(LocalDateTime.now())
                .role(Role.GUEST)
                .build());

        //then
        List<Member> list = memberRepository.findAll();
        assertEquals(name, list.get(0).getName());
        assertEquals(email, list.get(0).getEmail());
        System.out.println(list.get(0).getCreateAt());
    }
}