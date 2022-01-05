package com.example.usedmarket.web.order;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.PersistenceContext;

import java.util.List;

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

    private Setup setup = new Setup();
    private UserEntity userEntity;
    private UserPrincipal userPrincipal;
    private Book book;
    private Post post;
    private Order order;

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        book = setup.createBook();
        post = setup.createPost(userEntity);
        post.addBook(book);
        book.addPost(post);
        postRepository.save(post);
        order = setup.createOrder(userEntity, post);
    }

//    @AfterEach
//    void clean() {
//        userRepository.deleteAll();
//        postRepository.deleteAll();
//        orderRepository.deleteAll();
//    }

    @Test
    @DisplayName("주문 저장 및 조회")
    void save() {
        //given

        //when
        testEntityManager.persist(order);
        testEntityManager.clear();

        //then
        assertThat(orderRepository.findById(order.getId()).get().getAddress()).isEqualTo(order.getAddress());
    }

    @Test
    @DisplayName("USER ID 로 Order 정보 조회")
    void findByUserId() {
        //given

        //when
        testEntityManager.persist(order);
        testEntityManager.clear();
        List<Order> retrieveOrder = orderRepository.findByUserId(userPrincipal.getId());

        //then
        assertThat(retrieveOrder.get(0).getUser().getName()).isEqualTo(userPrincipal.getName());
        assertThat(orderRepository.findById(order.getId()).get().getAddress()).isEqualTo(order.getAddress());
    }
}