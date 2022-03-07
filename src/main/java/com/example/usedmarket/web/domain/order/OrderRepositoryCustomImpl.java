package com.example.usedmarket.web.domain.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.usedmarket.web.domain.order.QOrder.order;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@Slf4j
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // USER ID 에 의한 주문 조회
    @Override
    public List<Order> findByUserId(Long userId) {
        log.info("FIND ORDER BY USER ID");
        return jpaQueryFactory.selectFrom(order)
                .leftJoin(order.user, userEntity)
                .fetchJoin()
                .where(order.user.id.eq(userId))
                .fetch();
    }
}