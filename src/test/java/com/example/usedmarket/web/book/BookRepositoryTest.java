package com.example.usedmarket.web.book;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.QBook;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    EntityManager em;

    @Autowired
    BookRepository bookRepository;

    private Setup setup = new Setup();

    @DisplayName("BOOK 저장 및 조회 - queryDsl(JPAQueryFactory)")
    @Test
    void queryDslTest1() {

        Book createBook0 = setup.createBook();
        Book createBook1 = setup.createBook();
        bookRepository.saveAll(Arrays.asList(createBook0, createBook1));

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBook qBook = QBook.book;
        JPAQuery<Book> query = queryFactory.selectFrom(qBook)
                .where(qBook.stock.eq(1));

        List<Book> bookList = query.fetch();

        for (Book book : bookList) {
            System.out.println(book.toString());
        }
    }



    @DisplayName("BOOK 수정")
    @Test
    void bookUpdate() {
        //given
        Book createBook = setup.createBook();
        bookRepository.save(createBook);

        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        //when
        createBook.update(requestDto);
        bookRepository.flush();

        //then
        assertThat(createBook.getTitle()).isEqualTo(requestDto.getBookTitle());

    }


}