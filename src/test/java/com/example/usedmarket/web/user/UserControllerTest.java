package com.example.usedmarket.web.user;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.SignUpDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private Setup setup = new Setup();

    private MockMvc mvc;


    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

    }
//
//    @AfterEach
//    void clean() {
//        userRepository.deleteAll();
//    }


    @Test
    @DisplayName("본인 정보 조회")
    void getCurrentUser() throws Exception {
        //given
        SignUpDto requestDto = setup.createSignUpDto();
        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/users/me")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userEntity.getId()))
                .andExpect(jsonPath("$.userName").value(userEntity.getName()))
                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
                .andDo(print());

    }

//
//    @Test
//    @DisplayName("USER ID 를 이용한 사용자 조회 (ADMIN)")
//    void findByUserId() throws Exception {
//        //given
//        SignUpDto requestDto = setup.createSignUpDto();
//        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
//        userRepository.save(userEntity);
//        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
//
//        URI uri = UriComponentsBuilder.newInstance().scheme("http")
//                .host("localhost")
//                .port(port)
//                .path("/users/{id}")
//                .build().expand(userEntity.getId())
//                .encode()
//                .toUri();
//
//        //when
//        //then
//        mvc.perform(get(uri).with(user(userPrincipal))
//         .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userId").value(userEntity.getId()))
//                .andExpect(jsonPath("$.userName").value(userEntity.getName()))
//                .andExpect(jsonPath("$.email").value(userEntity.getEmail()))
//                .andDo(print());
//
//    }
//
//
//    @Test
//    @DisplayName("전체 USER 목록 조회 (ADMIN)")
//    void findAll() throws Exception {
//        //given
//        SignUpDto requestDto = setup.createSignUpDto();
//        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
//        userRepository.save(userEntity);
//        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
//
//        URI uri = UriComponentsBuilder.newInstance().scheme("http")
//                .host("localhost")
//                .port(port)
//                .path("/users/all")
//                .build().expand(userEntity.getId())
//                .encode()
//                .toUri();
//
//        //when
//        //then
//        mvc.perform(get(uri).with(user(userPrincipal))
//                       .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].userId").value(userEntity.getId()))
//                .andExpect(jsonPath("$.[0].userName").value(userEntity.getName()))
//                .andExpect(jsonPath("$.[0].email").value(userEntity.getEmail()))
//                .andDo(print());
//    }
//

    @Test
    @DisplayName("사용자 정보 수정")
    void updatePersonalInfo() throws Exception {
        //given
        SignUpDto requestDto = setup.createSignUpDto();
        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);

        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .userName("park")
                .build();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/users/me")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(put(uri).with(user(userPrincipal))
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(userUpdateRequestDto.getUserName()))
                .andDo(print());
    }


    @Test
    @DisplayName("회원 탈퇴")
    void delete() throws Exception {
        //given
        SignUpDto requestDto = setup.createSignUpDto();
        UserEntity userEntity = UserEntity.create(requestDto, passwordEncoder);
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);

        UserUpdateRequestDto userUpdateRequestDto = UserUpdateRequestDto.builder()
                .userName("park")
                .build();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/users/me")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(MockMvcRequestBuilders.delete(uri).with(user(userPrincipal))
                        .content(new ObjectMapper().writeValueAsString(userUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}
