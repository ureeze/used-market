package com.example.usedmarket.web.book;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;
import com.example.usedmarket.web.service.book.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookServiceTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookService bookService;

    private Setup setup = new Setup();
    private UserEntity userEntity;

    private Post post;
    private Book book0;
    private Book book1;

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity();
        userRepository.save(userEntity);

        post = setup.createPost(userEntity);
        book0 = setup.createBook();
        book1 = setup.createBook();
        post.addBook(book0);
        post.addBook(book1);
        book0.addPost(post);
        book1.addPost(post);
        postRepository.save(post);
    }

    @DisplayName("책 상세 조회")
    @Test
    void findById() {
        //given
//        System.out.println("post Title : " + post.getTitle());
//        System.out.println("book0 Title : " + book0.getTitle());
//        System.out.println("book1 Title : " + book1.getTitle());

        //when
        entityManager.clear();
        BookSearchListResponseDto findBook = bookService.findById(book0.getId());

        //then
//        System.out.println(findBook);
        assertThat(findBook.getPostTitle()).isEqualTo(post.getTitle());
        assertThat(findBook.getBookTitle()).isEqualTo(book0.getTitle());
    }


    @DisplayName("판매중인 도서 조회")
    @Test
    void findByStatusIsSell() {
        //given

        //when
        entityManager.clear();
        List<BookDetailsResponseDto> responseDtoList = bookService.findByStatusIsSell();

        //then
        assertThat(responseDtoList.get(0).getBookStatus()).isEqualTo(book0.getBookStatus().name());
        assertThat(responseDtoList.get(1).getBookStatus()).isEqualTo(book1.getBookStatus().name());
    }

    @DisplayName("등록된 도서 전체 조회")
    @Test
    void findAll() {
        //given

        //when
        entityManager.clear();
        List<BookDetailsResponseDto> responseDtoList = bookService.findAll();

        //then
        assertThat(responseDtoList.get(0).getBookTitle()).isEqualTo(book0.getTitle());
        assertThat(responseDtoList.get(1).getBookTitle()).isEqualTo(book1.getTitle());
    }

//    @DisplayName("도서 제목 검색")
//    @Test
//    void findByBookTitle() {
//        //given
//
//        //when
//        entityManager.clear();
//        List<BookDetailsResponseDto> responseDtoList = bookService.findByBookTitle("스프링부트");
//
//        //then
//        assertThat(responseDtoList.get(0).getBookTitle()).isEqualTo(book1.getTitle());
//        assertThat(responseDtoList.get(1).getBookTitle()).isEqualTo(book0.getTitle());
//    }
}