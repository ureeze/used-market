package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.repository.member.Member;
import com.example.usedmarket.web.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MemberRepository memberRepository;



    @Test
    @DisplayName("Member 등록 및 전체조회")
    public void memberCreate() throws Exception {
        //given
        String expectedName = "pbj";
        String expectedEmail = "pbj@google.com";
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name(expectedName)
                .email(expectedEmail)
                .build();

        //when
        webTestClient.post().uri("/members").bodyValue(requestDto).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo(expectedName)
                .jsonPath("$.email").isEqualTo(expectedEmail);

        //then
        List<Member> all = memberRepository.findAll();
        assertEquals(expectedName, all.get(0).getName());
        assertEquals(expectedEmail, all.get(0).getEmail());
    }

    @Test
    @DisplayName("Member 전체조회")
    public void memberRetrieve() throws Exception {
        //given
        String expectedName = "pbj";
        String expectedEmail = "pbj@google.com";


        //when
        webTestClient.get().uri("/members").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].name").isEqualTo(expectedName)
                .jsonPath("$.[0].email").isEqualTo(expectedEmail);

    }
}