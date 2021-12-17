package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.SessionMember;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
class PostControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

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

    Member createMember() {
        int num = (int) (Math.random() * 10000) + 1;
        return memberRepository.save(Member.builder()
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
    @DisplayName("controller - POST 등록 테스트")
    void save() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        PostSaveRequestDto requestDto = createPostSaveRequestDto();

        String url = "http://localhost:" + port + "/posts";

        //when
        mvc.perform(post(url).with(user(sessionMember))
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
    @DisplayName("controller - POST 전체조회 테스트")
    void findAll() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        PostSaveRequestDto requestDto = createPostSaveRequestDto();
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Book book = requestDto.toBook();
        Post post = requestDto.toPost(member, book);

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
        mvc.perform(get(uri).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].title").value(post.getTitle()));

        List<Book> list = bookRepository.findAll();
        assertThat(list.get(0).getBookName()).isEqualTo(book.getBookName());
    }

    @Test
    @DisplayName("controller - POST 조회 테스트")
    void findById() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto();
        Book book = requestDto0.toBook();
        Post post = requestDto0.toPost(member, book);
        postRepository.save(post);

        String url = "http://localhost:" + port + "/posts/" + post.getId();

        //when
        //then
        mvc.perform(get(url).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(post.getTitle()))
                .andExpect(jsonPath("content").value(post.getContent()))
                .andExpect(jsonPath("status").value(PostStatus.SELL.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("controller - POST 수정 테스트")
    void update() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto0 = createPostSaveRequestDto();
        Book book = requestDto0.toBook();
        Post post = requestDto0.toPost(member, book);
        postRepository.save(post);

        PostSaveRequestDto updateRequestDto = createPostSaveRequestDto();
        Book updateBook = updateRequestDto.toBook();
        Post updatePost = updateRequestDto.toPost(member, book);

        String url = "http://localhost:" + port + "/posts/" + post.getId();

//        //when
//        //then
//        mvc.perform(put(url).with(user(sessionMember))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(requestDto1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("title").value(post1.getTitle()))
//                .andExpect(jsonPath("content").value(post1.getContent()))
//                .andExpect(jsonPath("status").value("SELL"))
//                .andExpect(jsonPath("$.bookList").exists())
//                .andExpect(jsonPath("$.bookList[0].bookName").value(post1.getBookList().get(0).getBookName()))
//                .andExpect(jsonPath("$.bookList[0].stock").value(1))
//                .andDo(print());
    }

//    @Test
//    @DisplayName("controller - POST 삭제 테스트")
//    void deleteById() throws Exception {
//        //given
//        PostSaveRequestDto requestDto0 = createPostSaveRequestDto(0);
//        Member member = memberRepository.findByEmail(sessionMember.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
//
//        Post post0 = requestDto0.toPost(member);
////        Book book0 = requestDto0.toBook();
////        post0.getBookList().add(book0);
//        postRepository.save(post0);
//
//        String url = "http://localhost:" + port + "/posts/" + post0.getId();
//
//        //when
//        //then
//        mvc.perform(delete(url).with(user(sessionMember))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.postId").value(post0.getId()))
//                .andDo(print());
//    }


}
