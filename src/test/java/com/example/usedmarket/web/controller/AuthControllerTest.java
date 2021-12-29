package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - USER 등록 테스트")
    void signUp() throws Exception {
        //given
        String name = "PBJ";
        String email = "PBJ@google.com";
        SignUpDto signUpDto = SignUpDto.builder()
                .name(name)
                .email(email)
                .password("12341234")
                .build();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/auth/signup")
                .build()
                .encode()
                .toUri();

        //when
        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CONTROLLER - USER 조회 테스트")
    void login() throws Exception {
        //given
        String name = "PBJ";
        String email = "PBJ@google.com";
        String password = "123123";
        SignUpDto signUpDto = SignUpDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
        UserEntity userEntity = UserEntity.create(signUpDto, passwordEncoder);
        userRepository.save(userEntity);

        LoginRequestDto loginDto = LoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/auth/login")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andDo(print());


    }
}
