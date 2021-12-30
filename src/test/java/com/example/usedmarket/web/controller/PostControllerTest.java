package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.exception.UserNotFoundException;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
public class PostControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;

    PostSaveRequestDto createPostSaveRequestDto() {
        int num = (int) (Math.random() * 10000) + 1;
        return PostSaveRequestDto.builder()
                .title("TEST 제목" + num)
                .content("내용" + num)
                .bookName("책이름" + num)
                .stock(1)
                .unitPrice(10000)
                .category("경제" + num)
                .bookStatus("S")
                .imgUrl("img" + num)
                .build();

    }

    UserEntity createUserEntity() {
        int num = (int) (Math.random() * 10000) + 1;
        return userRepository.save(UserEntity.builder()
                .name("test" + num)
                .email("test" + num + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build());
    }

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();


    }

    @AfterEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("POST 등록 테스트")
    void save() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        PostSaveRequestDto requestDto = createPostSaveRequestDto();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts")
                .build()
                .encode()
                .toUri();

        //when
        mvc.perform(post(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(requestDto.getTitle()))
                .andExpect(jsonPath("content").value(requestDto.getContent()))
                .andExpect(jsonPath("status").value(PostStatus.SELL.name()))
                .andDo(print());

        //then
        Post post = postRepository.findAll().get(0);

    }

    @Test
    @DisplayName("POST 전체조회 테스트")
    void findAll() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        PostSaveRequestDto requestDto = createPostSaveRequestDto();
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity, book);

        postRepository.save(post);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].title").value(post.getTitle()));

        List<Book> list = bookRepository.findAll();
        assertThat(list.get(0).getBookName()).isEqualTo(book.getBookName());
    }

    @Test
    @DisplayName("POST 조회 테스트")
    void findById() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto();
        Book book = requestDto0.toBook();
        Post post = requestDto0.toPost(userEntity, book);
        postRepository.save(post);

        String url = "http://localhost:" + port + "/posts/" + post.getId();

        //when
        //then
        mvc.perform(get(url).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(post.getTitle()))
                .andExpect(jsonPath("content").value(post.getContent()))
                .andExpect(jsonPath("status").value(PostStatus.SELL.name()))
                .andDo(print());
    }
}
