package com.example.usedmarket.web.user;

import com.example.usedmarket.config.TestConfig;
import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private final Setup setup = new Setup();
    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity(0);
        testEntityManager.persist(userEntity);
    }

    @Test
    @DisplayName("저장 및 조회 테스트")
    void saveUserTest() {
        //given

        //when
        testEntityManager.clear();

        //then
        Optional<UserEntity> findUserEntity = userRepository.findById(userEntity.getId());
        findUserEntity.ifPresent(entity -> assertThat(entity).isEqualTo(userEntity));
    }


}