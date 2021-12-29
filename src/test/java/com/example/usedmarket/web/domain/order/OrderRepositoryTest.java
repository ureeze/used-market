package com.example.usedmarket.web.domain.order;

import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    UserEntity createUserEntity() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        return userRepository.save(userEntity);
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


    Order createOrder(UserEntity userEntity, Post post) {
        int num = (int) (Math.random() * 10000) + 1;
        return Order.builder()
                .recipient("pbj" + num)
                .address("seoul " + num)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .phone(num + "")
                .user(userEntity)
                .post(post)
                .build();
    }

    Order createOrder() {
        UserEntity userEntity = createUserEntity();
        Book book = createBook();
        Post post = createPost(userEntity, book);
        return createOrder(userEntity, post);
    }

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("REPOSITORY - 주문 저장 및 조회")
    void save() {
        //given
        Order order = createOrder();

        //when
        testEntityManager.persist(order);

        //then
        assertThat(orderRepository.findById(order.getId()).get().getAddress()).isEqualTo(order.getAddress());
    }


    @Test
    @DisplayName("REPOSITORY - 주문 취소")
    void cancel() {

    }
}
