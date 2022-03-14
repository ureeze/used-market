package com.example.usedmarket.web.orderedbook;

import com.example.usedmarket.config.TestConfig;
import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
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

    private final Setup setup = new Setup();
    private Book book;
    private OrderedBook orderedBook;


    @BeforeEach
    void setup() {
        UserEntity userEntity = setup.createUserEntity(0);
        testEntityManager.persist(userEntity);

        book = setup.createBook(0);
        Post post = setup.createPost(userEntity, 0);
        book.addPost(post);
        post.addBook(book);
        testEntityManager.persist(post);

        Order order = setup.createOrder(userEntity, null, 0);
        orderedBook = setup.createOrderedBook(userEntity, book);
        testEntityManager.persist(order);
    }

    @Test
    @DisplayName("OrderedBook 저장")
    void saveOrderedBook() {
        //given

        //when
        testEntityManager.persist(orderedBook);
        testEntityManager.clear();

        //then
        Optional<OrderedBook> findOrder = orderedBookRepository.findById(orderedBook.getId());
        if (findOrder.isPresent()) {
            assertThat(findOrder.get().getOrderPrice()).isEqualTo(orderedBook.getOrderPrice());
            assertThat(findOrder.get().getBook().getId()).isEqualTo(book.getId());
        }
    }
}