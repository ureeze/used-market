package com.example.usedmarket.web.post;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;

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

    @PersistenceContext
    EntityManager entityManager;

    MockMvc mvc;

    private Setup setup = new Setup();

    private UserEntity userEntity;
    private UserPrincipal userPrincipal;
    private PostSaveRequestDto requestDto;
    private Book book;
    private Post post;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();

        userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        requestDto = setup.createPostSaveRequestDto();

        book = requestDto.toBook();
        post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);
    }
//
//    @AfterEach
//    void clean() {
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    @DisplayName("POST 등록 테스트")
    void save() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(post(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postTitle").value(requestDto.getPostTitle()))
                .andExpect(jsonPath("$.postStatus").value(PostStatus.SELL.name()))
                .andDo(print());

        //then
        assertThat(postRepository.findAll().get(0).getTitle()).isEqualTo(requestDto.getPostTitle());
    }

    @Test
    @DisplayName("POST ID로 포스트 조회 테스트")
    void findById() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postTitle").value(post.getTitle()))
                .andExpect(jsonPath("$.postStatus").value(PostStatus.SELL.name()))
                .andDo(print());

        //then
        assertThat(postRepository.findById(post.getId()).get().getTitle()).isEqualTo(requestDto.getPostTitle());

    }

    @Test
    @DisplayName("POST 제목으로 포스트 목록 조회 테스트")
    void findByPostTitle() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/all/title")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(get(uri).with(user(userPrincipal))
                        .queryParam("postTitle", post.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].postTitle").value(post.getTitle()))
                .andExpect(jsonPath("$.[0].postStatus").value(PostStatus.SELL.name()))
                .andDo(print());

        //then
        assertThat(postRepository.findById(post.getId()).get().getTitle()).isEqualTo(requestDto.getPostTitle());
    }


    @Test
    @DisplayName("전체 POST 조회 테스트")
    void findAll() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/all")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].postTitle").value(post.getTitle()));

        //then
        assertThat(bookRepository.findAll().get(0).getTitle()).isEqualTo(book.getTitle());
    }


    @Test
    @DisplayName("POST 수정 테스트")
    void update() throws Exception {
        //given
        PostSaveRequestDto updateRequestDto = setup.createPostSaveRequestDto();

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(put(uri).with(user(userPrincipal))
                        .content(new ObjectMapper().writeValueAsString(updateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.postTitle").value(updateRequestDto.getPostTitle()));
    }


    @Test
    @DisplayName("POST 의 ID 을 이용해 POST 삭제 테스트")
    void delete() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/posts/{id}")
                .build().expand(post.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();
        mvc.perform(MockMvcRequestBuilders.delete(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$").value(post.getId()));
    }
}