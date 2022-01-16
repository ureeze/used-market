package com.example.usedmarket.web.order;

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
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.order.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class OrderServiceTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    OrderedBookRepository orderedBookRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderService orderService;

    private Setup setup = new Setup();
    private UserEntity userEntity;
    private UserPrincipal userPrincipal;
    private Book book;
    private Post post;
    private OrderRequestDto requestDto;
    private Order order0;
    private Order order1;
    private OrderedBook orderedBook;

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

        requestDto = setup.createOrderRequestDto(post, book);
        order0 = setup.createOrder(userEntity, post);
        order1 = setup.createOrder(userEntity, post);
        orderedBook = setup.createOrderedBook(userEntity, book);
        order0.addOrderedBook(orderedBook);
        order1.addOrderedBook(orderedBook);
        orderedBook.addOrder(order0);
        orderRepository.save(order0);
        orderRepository.save(order1);
    }
//    @AfterEach
//    void clean() {
//        orderRepository.deleteAll();
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    @DisplayName("주문 진행")
    void save() {
        //given

        //when
        entityManager.clear();
        OrderConfirmResponseDto responseDto = orderService.save(userPrincipal, requestDto);

        //then
        assertThat(responseDto.getAddress()).isEqualTo(requestDto.getAddress());
        assertThat(responseDto.getRecipient()).isEqualTo(requestDto.getRecipient());
    }

    @Test
    @DisplayName("주문 ID 값에 의한 주문 조회")
    void findById() {
        //given

        //when
        entityManager.clear();
        OrderConfirmResponseDto responseDto = orderService.findById(userPrincipal, order0.getId());

        //then
        assertThat(responseDto.getAddress()).isEqualTo(order0.getAddress());
        assertThat(responseDto.getBookTitle()).isEqualTo(orderedBook.getBook().getTitle());
    }

    @Test
    @DisplayName("해당 사용자에 대한 주문 전체 조회")
    void findAll() {
        //given

        //when
        entityManager.clear();
        List<OrderConfirmResponseDto> orderResponseDtoList = orderService.findAll(userPrincipal);

        //then
        assertThat(orderResponseDtoList.get(0).getBookTitle()).isEqualTo(order0.getOrderedBookList().get(0).getBook().getTitle());
        orderResponseDtoList.forEach(System.out::println);
    }

    @Test
    @DisplayName("주문 취소")
    void cancel() {
        //given

        //when
        entityManager.clear();
        orderService.cancel(userPrincipal, order0.getId());

        //then
        System.out.println("취소 완료");
    }
}