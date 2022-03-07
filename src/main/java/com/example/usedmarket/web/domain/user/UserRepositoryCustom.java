package com.example.usedmarket.web.domain.user;

import java.util.Optional;

public interface UserRepositoryCustom {
    // 사용자 EMAIL 로 USER ENTITY 조회
    Optional<UserEntity> findByEmail(String email);
}
