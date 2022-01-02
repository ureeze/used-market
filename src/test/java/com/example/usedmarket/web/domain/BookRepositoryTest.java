package com.example.usedmarket.web.domain;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.QBook;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.querydsl.core.BooleanBuilder;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BookRepository bookRepository;

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        return Book.builder()
                .title("bookTitle" + num)
                .category("it" + num)
                .imgUrl("url" + num)
                .unitPrice(10000)
                .bookStatus(BookStatus.S)
                .stock(1)
                .build();
    }

    PostSaveRequestDto createRequestDto() {
        int num = (int) (Math.random() * 10000) + 1;

        return PostSaveRequestDto.builder()
                .postTitle("스프링부트 책 팝니다." + num)
                .postContent("스프링부트는 스프링 프레임워크의 복잡한 환경설정을 간편하게 해놓은 ..." + num)
                .bookTitle("스프링부트로 앱 만들기" + num)
                .stock(1)
                .unitPrice(num)
                .bookCategory("it" + num)
                .bookStatus("S")
                .build();
    }

    @DisplayName("BOOK 저장 및 조회 - queryDsl(JPAQueryFactory)")
    @Test
    void queryDslTest1() {

        Book createBook1 = createBook();
        Book createBook2 = createBook();
        bookRepository.saveAll(Arrays.asList(createBook1, createBook2));

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
        Book createBook = createBook();
        bookRepository.save(createBook);

        PostSaveRequestDto requestDto = createRequestDto();

        //when
        createBook.update(requestDto);
        bookRepository.flush();

        //then
        assertThat(createBook.getTitle()).isEqualTo(requestDto.getBookTitle());

    }

//    @DisplayName("BOOK 삭제")
//    @Test
//    void bookDelete() {
//        //given
//        Book createBook = createBook();
//        bookRepository.save(createBook);
//
//        //when
//        bookRepository.deleteById(createBook.getId());
//
//        //then
//        assertThat(bookRepository.findById(createBook.getId())).isEqualTo(Optional.empty());
//    }
}