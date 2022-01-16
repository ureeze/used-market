package com.example.usedmarket.web.domain.book;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.usedmarket.web.domain.book.QBook.book;


public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public BookRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Book> findByBookTitle(String bookTitle) {
        return queryFactory.select(book)
                .from(book)
                .where(book.title.like("%" + bookTitle + "%"))
                .where(book.deleted.eq(false))
                .orderBy(book.createdAt.desc())
                .fetch();
    }
}
