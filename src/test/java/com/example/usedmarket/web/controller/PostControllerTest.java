package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class PostControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mvc;
    SessionMember sessionMember;
    Member member;

    public PostSaveRequestDto createPostSaveRequestDto(int num) {
        return PostSaveRequestDto.builder()
                .title("TEST 제목" + num)
                .content("내용" + num)
                .bookName("책이름" + num)
                .stock(1)
                .unitPrice(10000)
                .category("경제" + num)
                .imgUrl("img" + num)
                .build();

    }


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();

        member = Member.builder()
                .name("test")
                .email("test@google.com")
                .picture("pic")
                .role(Role.USER)
                .build();
        sessionMember = new SessionMember(memberRepository.save(member));

    }

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("controller - POST 등록 테스트")
    void save() throws Exception {
        //given
        PostSaveRequestDto requestDto = createPostSaveRequestDto(0);

        String url = "http://localhost:" + port + "/posts";

        //when
        mvc.perform(post(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(requestDto.getTitle()))
                .andExpect(jsonPath("content").value(requestDto.getContent()))
                .andExpect(jsonPath("status").value("SELL"))
                .andExpect(jsonPath("$.bookList").exists())
                .andExpect(jsonPath("$.bookList[0].stock").value(1))
                .andDo(print());

        //then
        Post post = postRepository.findAll().get(0);
        assertEquals(requestDto.getImgUrl(), post.getBookList().get(0).getImgUrl());

    }

    @Test
    @DisplayName("controller - POST 전체조회 테스트")
    void findAll() throws Exception {
        //given
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto(0);
        PostSaveRequestDto requestDto1 = createPostSaveRequestDto(1);
        Post post0 = Post.toPost(member, requestDto0);
        Post post1 = Post.toPost(member, requestDto1);
        Book book0 = Book.toBook(requestDto0);
        Book book1 = Book.toBook(requestDto1);
        post0.getBookList().add(book0);
        post0.getBookList().add(book1);
        post1.getBookList().add(book0);
        post1.getBookList().add(book1);

        postRepository.save(post0);
        postRepository.save(post1);

        String url = "http://localhost:" + port + "/posts";

        //when
        //then
        mvc.perform(get(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].title").value(post0.getTitle()))
                .andExpect(jsonPath("$.[1].title").value(post1.getTitle()))
                .andExpect(jsonPath("$.[0].bookList[0].bookName").value(post0.getBookList().get(0).getBookName()))
                .andExpect(jsonPath("$.[1].bookList[0].bookName").value(post1.getBookList().get(0).getBookName()));
    }

    @Test
    @DisplayName("controller - POST 조회 테스트")
    void findById() throws Exception {
        //given
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto(0);
        Post post0 = Post.toPost(member, requestDto0);
        Book book0 = Book.toBook(requestDto0);
        post0.getBookList().add(book0);
        postRepository.save(post0);

        String url = "http://localhost:" + port + "/posts/" + post0.getId();

        //when
        //then
        mvc.perform(get(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(post0.getTitle()))
                .andExpect(jsonPath("content").value(post0.getContent()))
                .andExpect(jsonPath("status").value("SELL"))
                .andExpect(jsonPath("$.bookList").exists())
                .andExpect(jsonPath("$.bookList[0].stock").value(1))
                .andExpect(jsonPath("$.bookList[0].bookName").value(post0.getBookList().get(0).getBookName()))
                .andDo(print());
    }

    @Test
    @DisplayName("controller - POST 수정 테스트")
    void update() throws Exception {
        //given
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto(0);
        Post post0 = Post.toPost(member, requestDto0);
        Book book0 = Book.toBook(requestDto0);
        post0.getBookList().add(book0);
        postRepository.save(post0);

        PostSaveRequestDto requestDto1 = createPostSaveRequestDto(1);
        Post post1 = Post.toPost(member, requestDto1);
        Book book1 = Book.toBook(requestDto1);
        post1.getBookList().add(book1);

        String url = "http://localhost:" + port + "/posts/" + post0.getId();

        //when
        //then
        mvc.perform(put(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(post1.getTitle()))
                .andExpect(jsonPath("content").value(post1.getContent()))
                .andExpect(jsonPath("status").value("SELL"))
                .andExpect(jsonPath("$.bookList").exists())
                .andExpect(jsonPath("$.bookList[0].bookName").value(post1.getBookList().get(0).getBookName()))
                .andExpect(jsonPath("$.bookList[0].stock").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("controller - POST 삭제 테스트")
    void deleteById() throws Exception {
        //given
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto(0);
        Post post0 = Post.toPost(member, requestDto0);
        Book book0 = Book.toBook(requestDto0);
        post0.getBookList().add(book0);
        postRepository.save(post0);

        String url = "http://localhost:" + port + "/posts/" + post0.getId();

        //when
        //then
        mvc.perform(delete(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post0.getId()))
                .andDo(print());
    }
}
