package com.example.usedmarket.web.orderedbook;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.OrderedBookDetailsResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.orderedBook.OrderedBookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderedBookServiceTest {
    @Autowired
    OrderedBookRepository orderedBookRepository;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderedBookService orderedBookService;

    private Setup setup = new Setup();

    @Test
    @DisplayName("주문한 책 조회")
    void findOne() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book = setup.createBook();
        bookRepository.save(book);

        Order order = setup.createOrder(userEntity, null);
        OrderedBook orderedBook = setup.createOrderedBook(userEntity, book);

        orderedBook.addOrder(order);
        order.addOrderedBook(orderedBook);
        orderRepository.save(order);

        entityManager.clear();

        //when
        OrderedBookDetailsResponseDto orderedBookDetailsResponseDto = orderedBookService.findOrderedBook(userPrincipal, orderedBook.getId());

        //then
        assertThat(orderedBookDetailsResponseDto.getBookTitle()).isEqualTo(book.getTitle());
        assertThat(orderedBookDetailsResponseDto.getId()).isEqualTo(orderedBook.getId());
    }

    @Test
    @DisplayName("주문한 책 목록 조회")
    void findAll() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        Book book0 = setup.createBook();
        Book book1 = setup.createBook();
        bookRepository.saveAll(new ArrayList<>(Arrays.asList(book0, book1)));

        Order order = setup.createOrder(userEntity, null);
        OrderedBook orderedBook0 = setup.createOrderedBook(userEntity, book0);
        OrderedBook orderedBook1 = setup.createOrderedBook(userEntity, book1);

        orderedBook0.addOrder(order);
        orderedBook1.addOrder(order);
        order.addOrderedBook(orderedBook0);
        order.addOrderedBook(orderedBook1);
        orderRepository.save(order);

        //when
        List<OrderedBookDetailsResponseDto> responseDtoList = orderedBookService.findByCurrentUser(userPrincipal);

        //then
        assertThat(responseDtoList.get(0).getBookTitle()).isEqualTo(book0.getTitle());
        assertThat(responseDtoList.get(0).getOrderPrice()).isEqualTo(orderedBook0.getOrderPrice());
        assertThat(responseDtoList.get(1).getBookTitle()).isEqualTo(book1.getTitle());
        assertThat(responseDtoList.get(1).getOrderPrice()).isEqualTo(orderedBook1.getOrderPrice());
    }
}