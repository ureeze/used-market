package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.security.dto.SessionMember;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
class OrderControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    WebApplicationContext context;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

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

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .bookName("bookTitle")
                .category("it")
                .imgUrl("url")
                .bookStatus(BookStatus.S)
                .unitPrice(10000)
                .stock(1)
                .build();
    }

    Post createPost(Member member, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        Post post = Post.builder()
                .title("PostTitle" + num)
                .content("contentInPost" + num)
                .status(PostStatus.SELL)
                .member(member)
                .build();
        post.addBook(book);
        return postRepository.save(post);
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

    OrderRequestDto createOrderRequestDto(Post post, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        return OrderRequestDto.builder()
                .recipient("PBJ" + num)
                .address("서울 영등포구 여의도동 32-1 " + num)
                .phone("01012345678")
                .count(1)
                .orderPrice(15000 + num)
                .postId(post.getId())
                .bookId(book.getId())
                .build();
    }


    @Test
    @DisplayName("주문 진행")
    void save() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Book book = createBook();
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).get();
        Post post = createPost(member, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);

        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders")
                .build()
                .encode()
                .toUri();


        //when
        //then
        mvc.perform(post(uri).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipient").value(requestDto.getRecipient()))
                .andExpect(jsonPath("$.address").value(requestDto.getAddress()))
                .andExpect(jsonPath("$.phone").value(requestDto.getPhone()))
                .andExpect(jsonPath("$.count").value(requestDto.getCount()))
                .andExpect(jsonPath("$.orderPrice").value(requestDto.getOrderPrice()))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 조회")
    void findById() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Book book = createBook();
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).get();
        Post post = createPost(member, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(member, post);
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);


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
        mvc.perform(get(uri).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient").value(savedOrder.getRecipient()))
                .andExpect(jsonPath("$.address").value(savedOrder.getAddress()))
                .andExpect(jsonPath("$.phone").value(savedOrder.getPhone()))
                .andExpect(jsonPath("$.deliveryStatus").value(savedOrder.getDeliveryStatus().name()))
                .andDo(print());
    }

    @Test
    @DisplayName("세션에 의한 주문 전체조회")
    void findAll() throws Exception {
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Book book = createBook();
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).get();
        Post post = createPost(member, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(member, post);
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);


        Order savedOrder = orderRepository.save(order);
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders")
                .build()
                .encode()
                .toUri();


        //when
        //then
        mvc.perform(get(uri).with(user(sessionMember))
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
    void cancel() throws Exception{
        //given
        SessionMember sessionMember = new SessionMember(createMember());
        Book book = createBook();
        Member member = memberRepository.findByEmail(sessionMember.getEmail()).get();
        Post post = createPost(member, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(member, post);
        OrderedBook orderedBook = requestDto.createOrderedBook(order, book);

        Order savedOrder = orderRepository.save(order);
        URI uri = UriComponentsBuilder.newInstance().scheme("http")
                .host("localhost")
                .port(8080)
                .path("/orders/{id}")
                .build().expand(order.getId())
                .encode()
                .toUri();

        //when
        //then
        mvc.perform(delete(uri).with(user(sessionMember))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.cancelOrderId").value(order.getId()))
                .andExpect(jsonPath("$.deliveryStatus").value(DeliveryStatus.CANCEL_COMPLETED.name()))
                .andDo(print());

    }
}