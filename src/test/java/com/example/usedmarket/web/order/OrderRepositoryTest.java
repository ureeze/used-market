package com.example.usedmarket.web.order;

import com.example.usedmarket.config.TestConfig;
import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class OrderRepositoryTest {
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    BookRepository bookRepository;
//
//    @Autowired
//    TestEntityManager testEntityManager;
//
//    private final Setup setup = new Setup();
//    private UserPrincipal userPrincipal;
//    private Order order;
//
//    @BeforeEach
//    void setup() {
//        UserEntity userEntity = setup.createUserEntity(0);
//        userRepository.save(userEntity);
//        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
//        Book book = setup.createBook(0);
//        Post post = setup.createPost(userEntity, 0);
//        post.addBook(book);
//        book.addPost(post);
//        postRepository.save(post);
//        order = setup.createOrder(userEntity, post, 0);
//    }
//
////    @AfterEach
////    void clean() {
////        userRepository.deleteAll();
////        postRepository.deleteAll();
////        orderRepository.deleteAll();
////    }
//
//    @Test
//    @DisplayName("주문 저장 및 조회")
//    void save() {
//        //given
//
//        //when
//        testEntityManager.persist(order);
//        testEntityManager.clear();
//
//        //then
//        Optional<Order> findOrder = orderRepository.findById(order.getId());
//        findOrder.ifPresent(value -> assertThat(value.getAddress()).isEqualTo(order.getAddress()));
//    }
//
//    @Test
//    @DisplayName("USER ID 로 Order 정보 조회")
//    void findByUserId() {
//        //given
//
//        //when
//        testEntityManager.persist(order);
//        testEntityManager.clear();
//
//        //then
//        List<Order> retrieveOrder = orderRepository.findByUserId(userPrincipal.getId());
//        assertThat(retrieveOrder.get(0).getUser().getName()).isEqualTo(userPrincipal.getName());
//
//        Optional<Order> findOrder = orderRepository.findById(order.getId());
//        findOrder.ifPresent(value -> assertThat(value.getAddress()).isEqualTo(order.getAddress()));
//    }
}