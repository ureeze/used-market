package com.example.usedmarket.web.domain.orderedBook;

import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OrderedBookRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    OrderedBookRepository orderedBookRepository;

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
                .bookName("bookTitle" + num)
                .category("it" + num)
                .imgUrl("url" + num)
                .unitPrice(10000 + num)
                .bookStatus(BookStatus.S)
                .stock(1)
                .build();
    }

    Post createPost(UserEntity userEntity) {
        int num = (int) (Math.random() * 10000) + 1;
        Post post = Post.builder()
                .title("PostTitle" + num)
                .content("contentInPost" + num)
                .status(PostStatus.SELL)
                .userEntity(userEntity)
                .build();
        return post;
    }

    Order createOrder(UserEntity userEntity, Post post) {
        int num = (int) (Math.random() * 10000) + 1;
        Order order = Order.builder()
                .recipient("pbj" + num)
                .address("seoul " + num)
                .deliveryStatus(DeliveryStatus.PAYMENT_COMPLETED)
                .phone(num + "")
                .user(userEntity)
                .post(post)
                .build();
        return order;
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
        userRepository.deleteAll();
        postRepository.deleteAll();
        orderRepository.deleteAll();
    }


    @Test
    @DisplayName("REPOSITORY - OrderedBook 저장")
    void save() {
        //given
        UserEntity userEntity = createUserEntity();
        Book book = createBook();
        Post post = createPost(userEntity);
        book.addPost(post);
        post.addBook(book);
        Post savedPost = postRepository.save(post);

        Order order = createOrder(userEntity, savedPost);
        OrderedBook orderedBook = createOrderedBook(order, book);
        testEntityManager.persist(order);

        //when
        testEntityManager.persist(orderedBook);

        //then
        assertThat(orderedBookRepository.findById(orderedBook.getId()).get().getOrderPrice()).isEqualTo(orderedBook.getOrderPrice());
    }
}
