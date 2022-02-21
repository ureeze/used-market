package com.example.usedmarket.web.domain.book;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.usedmarket.web.domain.book.QBook.book;


@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 도서 제목에 의한 도서 목록 조회
    @Override
    public Page<BookDetailsResponseDto> findByBookTitle(String bookTitle, Pageable pageable) {
        List<BookDetailsResponseDto> content = queryFactory.selectFrom(book)
                .where(book.title.like("%" + bookTitle + "%"), book.deleted.eq(false))
                .orderBy(book.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream().map(book1 -> BookDetailsResponseDto.toDto(book1)).collect(Collectors.toList());
        List<Book> bookList = queryFactory.selectFrom(book)
                .where(book.title.like("%" + bookTitle + "%"), book.deleted.eq(false))
                .fetch();
        return new PageImpl<>(content, pageable, bookList.size());

    }
}