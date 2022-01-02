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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .postTitle("웹서비스책 판매합니다." + num)
                .postContent("내용" + num)
                .bookTitle("웹서비스" + num)
                .stock(1)
                .unitPrice(10000)
                .bookCategory("경제" + num)
                .bookStatus("S")
                .bookImgUrl("img" + num)
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
                .andExpect(jsonPath("$.postTitle").value(requestDto.getPostTitle()))
                .andExpect(jsonPath("$.postStatus").value(PostStatus.SELL.name()))
                .andDo(print());

        //then
        Post post = postRepository.findAll().get(0);

    }

    @Test
    @DisplayName("POST ID로 포스트 조회 테스트")
    void findById() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto();
        Book book = requestDto0.toBook();
        Post post = requestDto0.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);
        postRepository.save(post);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postTitle").value(post.getTitle()))
                .andExpect(jsonPath("$.postStatus").value(PostStatus.SELL.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("POST 제목으로 포스트 목록 조회 테스트")
    void findByPostTitle() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto();
        Book book = requestDto0.toBook();
        Post post = requestDto0.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);
        postRepository.save(post);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/all/title")
                .build()
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .queryParam("postTitle", "웹서비스")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].postTitle").value(post.getTitle()))
                .andExpect(jsonPath("$.[0].postStatus").value(PostStatus.SELL.name()))
                .andDo(print());
    }


    @Test
    @DisplayName("전체 POST 조회 테스트")
    void findAll() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        PostSaveRequestDto requestDto = createPostSaveRequestDto();
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/all")
                .build()
                .encode()
                .toUri();

        //when
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].postTitle").value(post.getTitle()));

        //then
        List<Book> list = bookRepository.findAll();
        assertThat(list.get(0).getTitle()).isEqualTo(book.getTitle());
    }


    @Test
    @DisplayName("POST 수정 테스트")
    void update() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        PostSaveRequestDto requestDto = createPostSaveRequestDto();
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);

        PostSaveRequestDto newRequestDto = createPostSaveRequestDto();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        mvc.perform(put(uri).with(user(userPrincipal))
                        .content(new ObjectMapper().writeValueAsString(newRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.postTitle").value(newRequestDto.getPostTitle()));

    }


    @Test
    @DisplayName("POST 의 ID 을 이용해 POST 삭제 테스트")
    void delete() throws Exception {
        //given
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal((createUserEntity()));
        PostSaveRequestDto requestDto = createPostSaveRequestDto();
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);


        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        mvc.perform(MockMvcRequestBuilders.delete(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").value(post.getId()));

    }


}
