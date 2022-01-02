package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.PostStatus;
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

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .build();


    }

    OrderedBook createOrderedBook(UserEntity userEntity, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        return OrderedBook.builder()
                .amount(1)
                .orderPrice(10000 + num)
                .book(book)
                .user(userEntity)
                .build();
    }

    Order createOrder() {
        int num = (int) (Math.random() * 10000) + 1;
        Order order = Order.builder()
                .recipient("pbj" + num)
                .address("seoul " + num)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .phone(num + "")
                .build();
        return order;
    }

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .title("bookTitle" + num)
                .category("it" + num)
                .imgUrl("url" + num)
                .bookStatus(BookStatus.S)
                .unitPrice(10000 + num)
                .stock(1)
                .build();
    }

    Post createPost(UserEntity userEntity, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        Post post = Post.builder()
                .title("PostTitle" + num)
                .content("contentInPost" + num)
                .status(PostStatus.SELL)
                .userEntity(userEntity)
                .build();
        post.addBook(book);
        return postRepository.save(post);
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

    OrderRequestDto createOrderRequestDto(Post post, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        return OrderRequestDto.builder()
                .recipient("PBJ" + num)
                .address("서울 영등포구 여의도동 32-1 " + num)
                .phone("01012345678")
                .bookAmount(1)
                .orderPrice(15000 + num)
                .postId(post.getId())
                .bookId(book.getId())
                .build();
    }


    @Test
    @DisplayName("주문 요청")
    void save() throws Exception {
        //given
        Book book = createBook();
        UserEntity userEntity = createUserEntity();
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/orders")
                .build()
                .encode()
                .toUri();


        //when
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
        UserEntity userEntity = createUserEntity();
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book = createBook();
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);

        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);
        orderedBook.addOrder(order);
        order.addOrderedBook(orderedBook);

        Order savedOrder = orderRepository.save(order);
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/{id}")
                .build().expand(savedOrder.getId())
                .encode()
                .toUri();


        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.recipient").value(savedOrder.getRecipient()))
//                .andExpect(jsonPath("$.address").value(savedOrder.getAddress()))
//                .andExpect(jsonPath("$.phone").value(savedOrder.getPhone()))
//                .andExpect(jsonPath("$.deliveryStatus").value(savedOrder.getDeliveryStatus().name()))
                .andDo(print());
    }

    @Test
    @DisplayName("해당 사용자에 대한 주문 전체 조회")
    void findAll() throws Exception {
        //given
        UserEntity userEntity = createUserEntity();
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book = createBook();
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);
        orderedBook.addOrder(order);
        order.addOrderedBook(orderedBook);

        Order savedOrder = orderRepository.save(order);
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(port)
                .path("/orders/all/me")
                .build()
                .encode()
                .toUri();


        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].recipient").value(savedOrder.getRecipient()))
                .andExpect(jsonPath("$.[0].address").value(savedOrder.getAddress()))
                .andExpect(jsonPath("$.[0].phone").value(savedOrder.getPhone()))
                .andExpect(jsonPath("$.[0].deliveryStatus").value(savedOrder.getDeliveryStatus().name()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() throws Exception {
        //given
        UserEntity userEntity = createUserEntity();
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book = createBook();
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);
        orderedBook.addOrder(order);
        order.addOrderedBook(orderedBook);

        orderRepository.save(order);
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/{id}")
                .build().expand(order.getId())
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(delete(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.cancelOrderId").value(order.getId()))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CANCEL_COMPLETED.name()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문한 책 조회")
    void findOrderedBook() throws Exception {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book = createBook();
        bookRepository.save(book);

        Order order = createOrder();
        OrderedBook orderedBook = createOrderedBook(userEntity, book);

        orderedBook.addOrder(order);
        order.addOrderedBook(orderedBook);
        orderRepository.save(order);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/books/{id}")
                .build().expand(orderedBook.getId())
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(get(uri).with(user(userPrincipal))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderedBook.getId()))
                .andExpect(jsonPath("$.bookTitle").value(book.getTitle()))
                .andDo(print());
    }


    @Test
    @DisplayName("현재 사용자가 주문한 책 목록 조회")
    void findByCurrentUser() throws Exception {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book0 = createBook();
        Book book1 = createBook();
        bookRepository.saveAll(new ArrayList<>(Arrays.asList(book0, book1)));

        Order order = createOrder();
        OrderedBook orderedBook0 = createOrderedBook(userEntity, book0);
        OrderedBook orderedBook1 = createOrderedBook(userEntity, book1);

        orderedBook0.addOrder(order);
        orderedBook1.addOrder(order);
        order.addOrderedBook(orderedBook0);
        order.addOrderedBook(orderedBook1);
        orderRepository.save(order);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/books/me")
                .build()
                .encode()
                .toUri();

        //when
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
