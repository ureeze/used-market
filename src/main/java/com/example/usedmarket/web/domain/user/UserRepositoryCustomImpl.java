package com.example.usedmarket.web.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(userEntity)
                .where(userEntity.email.eq(email))
                .fetchOne());
    }
}