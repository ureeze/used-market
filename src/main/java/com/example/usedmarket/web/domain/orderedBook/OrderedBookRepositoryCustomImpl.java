package com.example.usedmarket.web.domain.orderedBook;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.usedmarket.web.domain.orderedBook.QOrderedBook.orderedBook;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@Slf4j
@RequiredArgsConstructor
public class OrderedBookRepositoryCustomImpl implements OrderedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 현재 사용자에 대한 ORDERED BOOK 조회
    @Override
    public List<OrderedBook> findByCurrentUser(Long userId) {
        log.info("FIND ORDERED BOOK BY CURRENT USER");
        return queryFactory.selectFrom(orderedBook)
                .leftJoin(orderedBook.user, userEntity)
                .fetchJoin()
                .where(orderedBook.user.id.eq(userId))
                .orderBy(orderedBook.id.asc())
                .fetch();
    }
}