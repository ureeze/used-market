package com.example.usedmarket.web.domain.order;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;


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

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .bookName("bookTitle")
                .category("it")
                .imgUrl("url")
                .unitPrice(10000)
                .bookStatus(BookStatus.S)
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


    Order createOrder(Member member, Post post) {
        int num = (int) (Math.random() * 10000) + 1;
        return Order.builder()
                .recipient("pbj" + num)
                .address("seoul " + num)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .phone(num + "")
                .member(member)
                .post(post)
                .build();
    }

    Order createOrder() {
        Member member = createMember();
        Book book = createBook();
        Post post = createPost(member, book);
        return createOrder(member, post);
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("REPOSITORY - 주문 저장 및 조회")
    void save() {
        //given
        Order order = createOrder();

        //when
        Order savedOrder = orderRepository.save(order);
        Order findOrder = orderRepository.findById(savedOrder.getId()).get();

        //then
        assertEquals(savedOrder.getId(), order.getId());
        assertEquals(savedOrder.getAddress(), order.getAddress());
        assertEquals(savedOrder.getDeliveryStatus(), order.getDeliveryStatus());
        assertEquals(savedOrder.getPhone(), order.getPhone());

        assertEquals(findOrder.getId(), savedOrder.getId());
        assertEquals(findOrder.getAddress(), savedOrder.getAddress());
        assertEquals(findOrder.getDeliveryStatus(), savedOrder.getDeliveryStatus());
        assertEquals(findOrder.getPhone(), savedOrder.getPhone());
    }


    @Test
    @DisplayName("REPOSITORY - 주문 취소")
    void cancel() {

    }
}