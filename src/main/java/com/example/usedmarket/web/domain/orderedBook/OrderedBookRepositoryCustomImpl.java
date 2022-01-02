package com.example.usedmarket.web.domain.orderedBook;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.usedmarket.web.domain.orderedBook.QOrderedBook.orderedBook;

public class OrderedBookRepositoryCustomImpl implements OrderedBookRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public OrderedBookRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<OrderedBook> findByCurrentUser(Long userId) {
        return queryFactory.select(orderedBook)
                .from(orderedBook)
                .where(orderedBook.user.id.eq(userId))
                .orderBy(orderedBook.id.asc())
                .fetch();
    }

}
