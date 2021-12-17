package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.security.dto.SessionMember;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderedBookRepository orderedBookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderService orderService;

    Member createMember() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        Member member = Member.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        return memberRepository.save(member);
    }

    SessionMember createSessionMember() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        Member member = Member.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        return new SessionMember(memberRepository.save(member));
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

    @AfterEach
    void clean() {
        //orderedBookRepository.deleteAll();
        orderRepository.deleteAll();
        postRepository.deleteAll();
        //bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("SERVICE - 주문 저장")
    void save() {
        //given
        SessionMember sessionMember = createSessionMember();
        Book book = createBook();
        Post post = createPost(sessionMember.toMember(), book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);

        //when
        OrderConfirmResponseDto responseDto = orderService.save(sessionMember, requestDto);

        //then
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhone(), responseDto.getPhone());
        assertEquals(requestDto.getRecipient(), responseDto.getRecipient());
        assertEquals(requestDto.getCount(), responseDto.getCount());
        assertEquals(requestDto.getOrderPrice(), orderedBookRepository.findById(responseDto.getOrderedBookId()).get().getOrderPrice());
    }

    @Test
    @DisplayName("SERVICE - 개별 주문 조회")
    void findById() {
        //given
        SessionMember sessionMember = createSessionMember();
        Book book = createBook();
        Post post = createPost(sessionMember.toMember(), book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(sessionMember.toMember(), post);
        requestDto.createOrderedBook(order, book);
        Order savedOrder = orderRepository.save(order);

        //when
        OrderConfirmResponseDto responseDto = orderService.findById(savedOrder.getId());

        //then
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhone(), responseDto.getPhone());
        assertEquals(requestDto.getRecipient(), responseDto.getRecipient());
        assertEquals(requestDto.getCount(), responseDto.getCount());
        assertEquals(requestDto.getOrderPrice(), orderedBookRepository.findById(responseDto.getOrderedBookId()).get().getOrderPrice());
    }

    @Test
    @DisplayName("SERVICE - 해당 세션의 전체 주문 조회")
    void findAll() {
        //given
        SessionMember sessionMember = createSessionMember();
        Book book = createBook();
        Post post = createPost(sessionMember.toMember(), book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(sessionMember.toMember(), post);
        requestDto.createOrderedBook(order, book);
        orderRepository.save(order);

        //when
        List<OrderConfirmResponseDto> orderResponseDtoList = orderService.findAll(sessionMember);

        //then
        assertEquals(requestDto.getAddress(), orderResponseDtoList.get(0).getAddress());
        assertEquals(requestDto.getPhone(), orderResponseDtoList.get(0).getPhone());
        assertEquals(requestDto.getRecipient(), orderResponseDtoList.get(0).getRecipient());
        assertEquals(requestDto.getCount(), orderResponseDtoList.get(0).getCount());
    }

    @Test
    @DisplayName("SERVICE - 주문 취소")
    void cancel() {
        //given
        Member member = createMember();
        Book book = createBook();
        Post post = createPost(member, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(member, post);
        requestDto.createOrderedBook(order, book);
        orderRepository.save(order);

        //when
        orderService.cancel(new SessionMember(member), order.getId());

        //then
    }
}