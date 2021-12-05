package com.example.usedmarket.web.domain.order;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    Member expectedMember;
    Post expectedPost;
    Book expectedBook;

    @BeforeEach
    void setUp() {
        expectedMember = Member.builder()
                .name("pbj")
                .email("pbj@naver.com")
                .picture("pic")
                .role(Role.USER)
                .build();
        memberRepository.save(expectedMember);

        expectedPost = Post.builder()
                .title("PostTitle")
                .content("contentInPost")
                .status(PostStatus.SELL)
                .member(expectedMember)
                .build();

        expectedBook = Book.builder()
                .bookName("bookTitle")
                .category("it")
                .imgUrl("url")
                .unitPrice(10000)
                .stock(1)
                .build();
        bookRepository.save(expectedBook);

        expectedPost.getBookList().add(expectedBook);
        postRepository.save(expectedPost);
    }

    @AfterEach
    void clean() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("REPOSITORY - ORDER 저장")
    public void save() {
        //given
        Order order = Order.builder()
                .address("seoul")
                .deliveryStatus(DeliveryStatus.BEING_DELIVERED)
                .phone("01012345678")
                .member(expectedMember)
                .post(expectedPost)
                .build();

        //when
        Order savedOrder = orderRepository.save(order);

        //then
        assertEquals(savedOrder.getId(), order.getId());
        assertEquals(savedOrder.getAddress(), order.getAddress());
        assertEquals(savedOrder.getDeliveryStatus(), order.getDeliveryStatus());
        assertEquals(savedOrder.getPhone(), order.getPhone());


    }
}