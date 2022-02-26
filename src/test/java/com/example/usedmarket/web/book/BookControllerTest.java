package com.example.usedmarket.web.book;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.security.dto.UserPrincipal;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerTest {

    @LocalServerPort
    int port;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WebApplicationContext context;

    private final Setup setup = new Setup();
    private MockMvc mvc;
    private UserPrincipal userPrincipal;
    private Book book;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();

        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);

        Post post = setup.createPost(userEntity);
        book = setup.createBook();
        post.addBook(book);
        book.addPost(post);
        postRepository.save(post);
    }


    @Test
    @DisplayName("책 상세 조회")
    void findById() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/books/{id}")
                .build().expand(book.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.bookTitle").value(book.getTitle()))
                .andExpect(jsonPath("$.bookStatus").value(BookStatus.S.name()));
    }

    @Test
    @DisplayName("판매중인 도서 조회")
    void findByStatusIsSell() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/books/all/sell")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].bookTitle").value(book.getTitle()))
                .andExpect(jsonPath("$.[0].bookStatus").value(BookStatus.S.name()));
    }

    @Test
    @DisplayName("등록된 도서 전체 조회")
    void findAll() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/books/all/sell")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].bookTitle").value(book.getTitle()))
                .andExpect(jsonPath("$.[0].bookStatus").value(BookStatus.S.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("도서 제목 검색")
    void findByBookTitle() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/books/all/title")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .queryParam("title", book.getTitle())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].bookTitle").value(book.getTitle()))
                .andDo(print());
    }
}