package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.SignUpDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    SignUpDto createSignUpDto() {
        int num = (int) (Math.random() * 10000) + 1;

        String name = "PBJ" + num;
        String email = name + "@google.com";
        SignUpDto requestDto = SignUpDto.builder()
                .name(name)
                .email(email)
                .password(num + "")
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
        userRepository.deleteAll();
    }

//    @Test
//    @WithMockUser(roles = "USER")
//    @DisplayName("CONTROLLER - USER 등록 테스트")
//    void signUp() throws Exception {
//        //given
//        SignUpDto requestDto = createSignUpDto();
//
//        URI uri = UriComponentsBuilder.newInstance().scheme("http")
//                .host("localhost")
//                .port(port)
//                .path("/auth/signup")
//                .build()
//                .encode()
//                .toUri();
//
//        //when
//        mvc.perform(post(uri)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isCreated())
//                .andDo(print());
//
//    }

    @Test
    @WithMockUser(username = "pbj", roles = "USER")
    @DisplayName("CONTROLLER - USER 전체조회")
    void userRetrieve() throws Exception {
        //given
        SignUpDto requestDto = createSignUpDto();
        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
        userRepository.save(userEntity);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/users/{id}")
                .build().expand(userEntity.getId())
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userEntity.getId()))
                .andExpect(jsonPath("$.name").value(userEntity.getName()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andDo(print());
    }

//    @Test
//    @WithMockUser(roles = "USER")
//    @DisplayName("CONTROLLER - Request 유효성 검증 테스트 (이름 Null)")
//    void validationRequestDto() throws Exception {
//        //given
//        MemberRequestDto requestDto = new MemberRequestDto();
//        requestDto.setEmail("pbj@naver.com");
//
//        String url = "http://localhost:" + port + "/members";
//
//        //when
//        //then
//        mvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    @DisplayName("CONTROLLER - Request 유효성 검증 테스트 (이메일 @ 누락)")
//    void validationRequestDto2() throws Exception {
//        //given
//        MemberRequestDto requestDto = MemberRequestDto.builder()
//                .name("pbj")
//                .email("pppbbbjjj")
//                .build();
//
//        String url = "http://localhost:" + port + "/members";
//
//        //when
//        //then
//        mvc.perform(post(url)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto)))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }
//
//    @Test
//    @WithMockUser(roles = "USER")
//    @DisplayName("CONTROLLER - Member 개별조회 UserNotFoundException 예외 테스트")
//    void memberFindById_userNotFoundExceptionTest() throws Exception {
//        //given
//        String url = "http://localhost:" + port + "/members/" + 100;
//
//        //when
//        //then
//        mvc.perform(get(url))
//                .andExpect(status().isNotFound())
//                .andDo(print());
//    }


}
