package com.example.usedmarket.web.domain.orderedBook;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import org.junit.jupiter.api.AfterEach;
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
class OrderedBookRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderedBookRepository orderedBookRepository;

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
                .bookName("bookTitle" + num)
                .category("it" + num)
                .imgUrl("url" + num)
                .unitPrice(10000 + num)
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
        Order order = Order.builder()
                .recipient("pbj" + num)
                .address("seoul " + num)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .phone(num + "")
                .member(member)
                .post(post)
                .build();
        return orderRepository.save(order);
    }

    OrderedBook createOrderedBook(Order order, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        return OrderedBook.builder()
                .count(1)
                .orderPrice(10000 + num)
                .order(order)
                .book(book)
                .build();
    }


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        bookRepository.deleteAll();
        orderRepository.deleteAll();
        orderedBookRepository.deleteAll();
    }


    @Test
    @DisplayName("REPOSITORY - OrderedBook 저장")
    void save() {
        //given
        Member member = createMember();
        Book book = createBook();
        Post post = createPost(member, book);
        Order order = createOrder(member, post);
        OrderedBook orderedBook = createOrderedBook(order, book);

        //when
        OrderedBook savedOrderedBook = orderedBookRepository.save(orderedBook);

        //then
        assertEquals(orderedBook.getCount(), savedOrderedBook.getCount());
        assertEquals(orderedBook.getOrderPrice(), savedOrderedBook.getOrderPrice());
        assertEquals(orderedBook.getBook().getBookName(), savedOrderedBook.getBook().getBookName());
        assertEquals(orderedBook.getOrder().getAddress(), savedOrderedBook.getOrder().getAddress());
    }
}