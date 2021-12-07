package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
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

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - Member 등록 테스트")
    void memberCreate() throws Exception {
        //given
        MemberRequestDto requestDto = createRequestDto();

        String url = "http://localhost:" + port + "/members";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andDo(print());

        //then
        List<Member> all = memberRepository.findAll();
        assertEquals(requestDto.getName(), all.get(0).getName());
        assertEquals(requestDto.getEmail(), all.get(0).getEmail());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - Member 전체조회")
    void memberRetrieve() throws Exception {
        //given
        MemberRequestDto requestDto = createRequestDto();

        memberRepository.save(requestDto.toMember());

        String url = "http://localhost:" + port + "/members";

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value(requestDto.getName()))
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - Request 유효성 검증 테스트 (이름 Null)")
    void validationRequestDto() throws Exception {
        //given
        MemberRequestDto requestDto = new MemberRequestDto();
        requestDto.setEmail("pbj@naver.com");

        String url = "http://localhost:" + port + "/members";

        //when
        //then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - Request 유효성 검증 테스트 (이메일 @ 누락)")
    void validationRequestDto2() throws Exception {
        //given
        MemberRequestDto requestDto = MemberRequestDto.builder()
                .name("pbj")
                .email("pppbbbjjj")
                .build();

        String url = "http://localhost:" + port + "/members";

        //when
        //then
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - Member 개별조회 UserNotFoundException 예외 테스트")
    void memberFindById_userNotFoundExceptionTest() throws Exception {
        //given
        String url = "http://localhost:" + port + "/members/" + 1;

        //when
        //then
        mvc.perform(get(url))
                .andExpect(status().isNotFound())
                .andDo(print());
    }



}