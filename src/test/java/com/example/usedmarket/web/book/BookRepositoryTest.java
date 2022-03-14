package com.example.usedmarket.web.book;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.QBook;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    private final Setup setup = new Setup();
    private Book createBook0;

    @BeforeEach
    void setup() {
        createBook0 = setup.createBook(0);
        Book createBook1 = setup.createBook(1);
        bookRepository.saveAll(Arrays.asList(createBook0, createBook1));
    }

    @DisplayName("BOOK 저장 및 조회 - queryDsl(JPAQueryFactory)")
    @Test
    void queryDslTest1() {
        //given
        entityManager.clear();

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QBook qBook = QBook.book;
        JPAQuery<Book> query = queryFactory.selectFrom(qBook)
                .where(qBook.stock.eq(1));

        //when
        List<Book> bookList = query.fetch();

        //then
        for (Book book : bookList) {
            System.out.println(book.toString());
        }
    }


    @DisplayName("BOOK 수정")
    @Test
    void bookUpdate() {
        //given
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto(0);

        //when
        createBook0.update(requestDto);
        bookRepository.flush();
        entityManager.clear();

        //then
        assertThat(createBook0.getTitle()).isEqualTo(requestDto.getBookTitle());
    }
}