package com.example.usedmarket.web.order;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.OrderRequestDto;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

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
public class OrderControllerTest {
    @LocalServerPort
    int port;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderRepository orderRepository;

    MockMvc mvc;

    private Setup setup = new Setup();

    private UserEntity userEntity;
    private UserPrincipal userPrincipal;
    private Book book0;
    private Book book1;
    private Post post;
    private Order order;
    private OrderedBook orderedBook0;
    private OrderedBook orderedBook1;


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

        book0 = setup.createBook();
        book1 = setup.createBook();
        post = setup.createPost(userEntity);
        book0.addPost(post);
        book1.addPost(post);
        post.addBook(book0);
        post.addBook(book1);
        postRepository.save(post);

        order = setup.createOrder(userEntity, post);
        orderedBook0 = setup.createOrderedBook(userEntity, book0);
        orderedBook1 = setup.createOrderedBook(userEntity, book1);
        orderedBook0.addOrder(order);
        orderedBook1.addOrder(order);
        order.addOrderedBook(orderedBook0);
        order.addOrderedBook(orderedBook1);
        orderRepository.save(order);
    }

    @Test
    @DisplayName("주문 요청")
    void save() throws Exception {
        //given
        OrderRequestDto requestDto = setup.createOrderRequestDto(post, book0);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/orders")
                .build()
                .encode()
                .toUri();


        //when
        entityManager.clear();

        //then
        mvc.perform(post(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipient").value(requestDto.getRecipient()))
                .andExpect(jsonPath("$.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(requestDto.getPhone()))
                .andExpect(jsonPath("$.bookAmount").value(requestDto.getBookAmount()))
                .andExpect(jsonPath("$.orderPrice").value(requestDto.getOrderPrice()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 ID 값에 의한 주문 조회")
    void findById() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/{id}")
                .build().expand(order.getId())
                .encode()
                .toUri();


        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient").value(order.getRecipient()))
                .andExpect(jsonPath("$.address").value(order.getAddress()))
                .andExpect(jsonPath("$.phone").value(order.getPhone()))
                .andExpect(jsonPath("$.deliveryStatus").value(order.getDeliveryStatus().getValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 사용자에 대한 주문 전체 조회")
    void findAll() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/orders/all/me")
                .build()
                .encode()
                .toUri();


        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].recipient").value(order.getRecipient()))
                .andExpect(jsonPath("$.[0].address").value(order.getAddress()))
                .andExpect(jsonPath("$.[0].phone").value(order.getPhone()))
                .andExpect(jsonPath("$.[0].deliveryStatus").value(order.getDeliveryStatus().getValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/{id}")
                .build().expand(order.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(delete(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cancelOrderId").value(order.getId()))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CANCEL_COMPLETED.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문한 책 조회")
    void findOrderedBook() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/books/{id}")
                .build().expand(orderedBook0.getId())
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderedBook0.getId()))
                .andExpect(jsonPath("$.bookTitle").value(book0.getTitle()))
                .andDo(print());
    }


    @Test
    @DisplayName("현재 사용자가 주문한 책 목록 조회")
    void findByCurrentUser() throws Exception {
        //given
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/books/me")
                .build()
                .encode()
                .toUri();

        //when
        entityManager.clear();

        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(orderedBook0.getId()))
                .andExpect(jsonPath("$.[0].bookTitle").value(book0.getTitle()))
                .andExpect(jsonPath("$.[1].id").value(orderedBook1.getId()))
                .andExpect(jsonPath("$.[1].bookTitle").value(book1.getTitle()))
                .andDo(print());
    }
}
