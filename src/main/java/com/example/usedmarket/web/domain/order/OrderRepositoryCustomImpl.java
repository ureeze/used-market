package com.example.usedmarket.web.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.usedmarket.web.domain.order.QOrder.order;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(order)
                .leftJoin(order.user, userEntity)
                .fetchJoin()
                .where(order.user.id.eq(userId))
                .fetch();
    }
}