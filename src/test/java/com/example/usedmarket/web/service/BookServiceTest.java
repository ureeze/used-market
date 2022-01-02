package com.example.usedmarket.web.service;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookResponseDto;
import com.example.usedmarket.web.service.book.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookServiceTest {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;


    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .title("깃허브와 깃" + num)
                .category("it" + num)
                .imgUrl("url" + num)
                .bookStatus(BookStatus.S)
                .unitPrice(10000)
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

    UserEntity createUserEntity() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        return UserEntity.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
    }

    @DisplayName("책 상세 조회")
    @Test
    void findById() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);

        Post post = createPost(userEntity);
        Book book = createBook();
        post.addBook(book);
        book.addPost(post);

        postRepository.save(post);

        //when
        BookDetailsResponseDto responseDto = bookService.findById(book.getId());

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(post.getTitle());
        assertThat(responseDto.getBookTitle()).isEqualTo(book.getTitle());
    }


    @DisplayName("판매중인 도서 조회")
    @Test
    void findByStatusIsSell() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);

        Post post = createPost(userEntity);
        Book book1 = createBook();
        Book book2 = createBook();
        post.addBook(book1);
        post.addBook(book2);
        book1.addPost(post);
        book2.addPost(post);

        postRepository.save(post);

        //when
        List<BookResponseDto> responseDtoList = bookService.findByStatusIsSell();

        //then
        assertThat(responseDtoList.get(0).getBookStatus()).isEqualTo(book1.getBookStatus());
        assertThat(responseDtoList.get(1).getBookStatus()).isEqualTo(book2.getBookStatus());
    }

    @DisplayName("등록된 도서 전체 조회")
    @Test
    void findAll() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);

        Post post = createPost(userEntity);
        Book book1 = createBook();
        Book book2 = createBook();
        post.addBook(book1);
        post.addBook(book2);
        book1.addPost(post);
        book2.addPost(post);

        postRepository.save(post);

        //when
        List<BookResponseDto> responseDtoList = bookService.findAll();

        //then
        assertThat(responseDtoList.get(0).getBookTitle()).isEqualTo(book1.getTitle());
        assertThat(responseDtoList.get(1).getBookTitle()).isEqualTo(book2.getTitle());
    }

    @DisplayName("도서 제목 검색")
    @Test
    void findByBookTitle() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);

        Post post = createPost(userEntity);
        Book book1 = createBook();
        Book book2 = createBook();
        post.addBook(book1);
        post.addBook(book2);
        book1.addPost(post);
        book2.addPost(post);

        postRepository.save(post);

        //when
        List<BookResponseDto> responseDtoList = bookService.findByBookTitle("깃허브");

        //then
        assertThat(responseDtoList.get(0).getBookTitle()).isEqualTo(book1.getTitle());
        assertThat(responseDtoList.get(1).getBookTitle()).isEqualTo(book2.getTitle());
    }
}