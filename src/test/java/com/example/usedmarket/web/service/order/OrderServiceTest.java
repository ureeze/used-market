package com.example.usedmarket.web.service.order;

import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.order.OrderRepository;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.OrderConfirmResponseDto;
import com.example.usedmarket.web.dto.OrderRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class OrderServiceTest {

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
    OrderServiceImpl orderService;

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

    UserPrincipal createUserPrincipal() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        UserEntity userEntity = UserEntity.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        userRepository.save(userEntity);
        return UserPrincipal.createUserPrincipal(userEntity);
    }

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .bookName("bookTitle")
                .category("it")
                .imgUrl("url")
                .bookStatus(BookStatus.S)
                .unitPrice(10000)
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


    OrderRequestDto createOrderRequestDto(Post post, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        return OrderRequestDto.builder()
                .recipient("PBJ" + num)
                .address("서울 영등포구 여의도동 32-1 " + num)
                .phone("01012345678")
                .count(1)
                .orderPrice(15000 + num)
                .postId(post.getId())
                .bookId(book.getId())
                .build();
    }

    @AfterEach
    void clean() {
        //orderedBookRepository.deleteAll();
        orderRepository.deleteAll();
        postRepository.deleteAll();
        //bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("SERVICE - 주문 저장")
    void save() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        Book book = createBook();
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);

        //when
        OrderConfirmResponseDto responseDto = orderService.save(userPrincipal, requestDto);

        //then
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhone(), responseDto.getPhone());
        assertEquals(requestDto.getRecipient(), responseDto.getRecipient());
        assertEquals(requestDto.getCount(), responseDto.getCount());
        assertEquals(requestDto.getOrderPrice(), orderedBookRepository.findById(responseDto.getOrderedBookId()).get().getOrderPrice());
    }

    @Test
    @DisplayName("SERVICE - 개별 주문 조회")
    void findById() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        Book book = createBook();
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);
        requestDto.createOrderedBook(order, book);
        Order savedOrder = orderRepository.save(order);

        //when
        OrderConfirmResponseDto responseDto = orderService.findById(savedOrder.getId());

        //then
        assertEquals(requestDto.getAddress(), responseDto.getAddress());
        assertEquals(requestDto.getPhone(), responseDto.getPhone());
        assertEquals(requestDto.getRecipient(), responseDto.getRecipient());
        assertEquals(requestDto.getCount(), responseDto.getCount());
        assertEquals(requestDto.getOrderPrice(), orderedBookRepository.findById(responseDto.getOrderedBookId()).get().getOrderPrice());
    }

    @Test
    @DisplayName("SERVICE - 해당 세션의 전체 주문 조회")
    void findAll() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        Book book = createBook();
        UserEntity userEntity = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);
        requestDto.createOrderedBook(order, book);
        orderRepository.save(order);

        //when
        List<OrderConfirmResponseDto> orderResponseDtoList = orderService.findAll(userPrincipal);

        //then
        assertEquals(requestDto.getAddress(), orderResponseDtoList.get(0).getAddress());
        assertEquals(requestDto.getPhone(), orderResponseDtoList.get(0).getPhone());
        assertEquals(requestDto.getRecipient(), orderResponseDtoList.get(0).getRecipient());
        assertEquals(requestDto.getCount(), orderResponseDtoList.get(0).getCount());
    }

    @Test
    @DisplayName("SERVICE - 주문 취소")
    void cancel() {
        //given
        UserEntity userEntity = createUserEntity();
        Book book = createBook();
        Post post = createPost(userEntity, book);
        OrderRequestDto requestDto = createOrderRequestDto(post, book);
        Order order = requestDto.createOrder(userEntity, post);
        requestDto.createOrderedBook(order, book);
        orderRepository.save(order);

        //when
        orderService.cancel(UserPrincipal.createUserPrincipal(userEntity), order.getId());

        //then
        System.out.println("취소 완료");
    }
}
