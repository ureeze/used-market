package com.example.usedmarket.web.domain.orderedBook;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.usedmarket.web.domain.orderedBook.QOrderedBook.orderedBook;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@RequiredArgsConstructor
public class OrderedBookRepositoryCustomImpl implements OrderedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderedBook> findByCurrentUser(Long userId) {
        return queryFactory.selectFrom(orderedBook)
//                .leftJoin(orderedBook.user, userEntity)
//                .fetchJoin()
                .where(orderedBook.user.id.eq(userId))
                .orderBy(orderedBook.id.asc())
                .fetch();
    }
}