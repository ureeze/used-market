package com.example.usedmarket.web.domain.user;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<UserEntity> findByEmail(String email);
}
