package com.example.usedmarket.web;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.order.DeliveryStatus;
import com.example.usedmarket.web.domain.order.Order;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.dto.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Setup {
    //UserEntity
    private final String USER_NAME = "PBJ";
    private final String UPDATED_USER_NAME = "PARK";
    private final String EMAIL = USER_NAME + "@gmail.com";
    private final String PASSWORD = "12341234";
    private final String ENCRYPTION_PASSWORD = new BCryptPasswordEncoder().encode(PASSWORD);
    private final String PICTURE = "google.com/picture000";

    //POST
    private final String POST_TITLE = "스프링부트 책 팝니다.";
    private final String POST_CONTENT = "스프링부트는 스프링 프레임워크의 복잡한 환경설정을 간편하게 해놓은 ...";
    private final PostStatus POST_STATUS = PostStatus.SELL;

    //BOOK
    private final String BOOK_TITLE = "스프링부트 2.0 퀵 스타트";
    private final String BOOK_CATEGORY = "IT/컴퓨터";
    private final String BOOK_IMG_URL = "google.com/img000";
    private final BookStatus BOOK_STATUS = BookStatus.S;
    private final int BOOK_UNIT_PRICE = 10000;
    private final int BOOK_STOCK = 1;

    //ORDER
    private final String ORDER_RECIPIENT = USER_NAME;
    private final String ORDER_ADDRESS = "서울특별시 용산구";
    private final DeliveryStatus ORDER_DELIVERY_STATUS = DeliveryStatus.PAYMENT_COMPLETED;
    private final String ORDER_PHONE = "01023456789";

    //ORDERED_BOOK
    private final int ORDERED_BOOK_AMOUNT = 1;
    private final int ORDERED_BOOK_PRICE = 10000;


    //난수
    public int getRandomNum() {
        int num = (int) (Math.random() * 10000) + 1;
        return num;
    }

    public UserEntity createUserEntity() {
        return UserEntity.builder()
                .name(USER_NAME)
                .password(ENCRYPTION_PASSWORD)
                .email(EMAIL)
                .picture(PICTURE)
                .registrationId(null)
                .role(Role.USER)
                .build();
    }

    public Post createPost(UserEntity userEntity) {
        int num = getRandomNum();
        return Post.builder()
                .title(POST_TITLE + num)
                .content(POST_CONTENT + num)
                .status(POST_STATUS)
                .userEntity(userEntity)
                .build();
    }

    public Book createBook() {
        int num = getRandomNum();
        return Book.builder()
                .title(BOOK_TITLE + num)
                .category(BOOK_CATEGORY + num)
                .imgUrl(BOOK_IMG_URL + num)
                .bookStatus(BOOK_STATUS)
                .unitPrice(BOOK_UNIT_PRICE + num)
                .stock(BOOK_STOCK)
                .build();
    }

    public Order createOrder(UserEntity userEntity, Post post) {
        int num = getRandomNum();
        return Order.builder()
                .recipient(ORDER_RECIPIENT + num)
                .address(ORDER_ADDRESS + num)
                .deliveryStatus(ORDER_DELIVERY_STATUS)
                .phone(ORDER_PHONE + "")
                .post(post)
                .userEntity(userEntity)
                .build();
    }

    public OrderedBook createOrderedBook(UserEntity userEntity, Book book) {
        return OrderedBook.builder()
                .amount(ORDERED_BOOK_AMOUNT)
                .orderPrice(ORDERED_BOOK_PRICE)
                .book(book)
                .user(userEntity)
                .build();
    }

    public SignUpRequestDto createSignUpDto() {
        return SignUpRequestDto.builder()
                .userName(USER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    public LoginRequestDto createLoginRequestDto() {
        return LoginRequestDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    public UserUpdateRequestDto createUserUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .userName(UPDATED_USER_NAME)
                .build();
    }

    public PostSaveRequestDto createPostSaveRequestDto() {
        int num = getRandomNum();
        return PostSaveRequestDto.builder()
                .postTitle(POST_TITLE + num)
                .postContent(POST_CONTENT + num)
                .bookTitle(BOOK_TITLE + num)
                .stock(BOOK_STOCK)
                .unitPrice(BOOK_UNIT_PRICE)
                .bookCategory(BOOK_CATEGORY + num)
                .bookStatus(BOOK_STATUS.name())
                .build();
    }

    public OrderRequestDto createOrderRequestDto(Post post, Book book) {
        int num = getRandomNum();
        return OrderRequestDto.builder()
                .recipient(ORDER_RECIPIENT + num)
                .address(ORDER_ADDRESS + num)
                .phone(ORDER_PHONE)
                .bookAmount(ORDERED_BOOK_AMOUNT)
                .orderPrice(ORDERED_BOOK_PRICE + num)
                .postId(post.getId())
                .bookId(book.getId())
                .build();
    }
}