package com.example.usedmarket.web.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 사용자 EMAIL 로 USER ENTITY 조회
    @Override
    public Optional<UserEntity> findByEmail(String email) {
        log.info("FIND USER ENTITY BY USER EMAIL");
        return Optional.ofNullable(jpaQueryFactory.selectFrom(userEntity)
                .where(userEntity.email.eq(email))
                .fetchOne());
    }
}