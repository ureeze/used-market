package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @AfterEach
    public void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Member 등록 테스트")
    public void memberCreate() throws Exception {
        //given
        String expectedName = "pbj";
        String expectedEmail = "pbj@google.com";
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name(expectedName)
                .email(expectedEmail)
                .build();


        String url = "http://localhost:" + port + "/members";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        //then
        List<Member> all = memberRepository.findAll();
        assertEquals(expectedName, all.get(0).getName());
        assertEquals(expectedEmail, all.get(0).getEmail());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Member 전체조회")
    public void memberRetrieve() throws Exception {
        //given
        String expectedName = "pbj1";
        String expectedEmail = "pbj1@google.com";

        memberRepository.save(Member.builder()
                .name(expectedName)
                .email(expectedEmail)
                .picture("pic")
                .role(Role.USER)
                .build());

        String url = "http://localhost:" + port + "/members";

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(expectedName))
                .andExpect(jsonPath("$.[0].id").isNumber());

    }
}