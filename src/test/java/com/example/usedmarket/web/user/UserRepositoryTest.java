package com.example.usedmarket.web.user;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private Setup setup = new Setup();

    @Test
    @DisplayName("저장 테스트")
    void saveUserTest() {
        //given
        UserEntity userEntity = setup.createUserEntity();

        //when
        testEntityManager.persist(userEntity);

        //then
        assertThat(userRepository.findById(userEntity.getId()).get()).isEqualTo(userEntity);
    }


    @Test
    @DisplayName("조회 테스트")
    void findMemberTest() {
        //given
        UserEntity userEntity0 = setup.createUserEntity();
        UserEntity userEntity1 = setup.createUserEntity();

        //when
        testEntityManager.persist(userEntity0);
        testEntityManager.persist(userEntity1);

        //then
        assertThat(userRepository.findById(userEntity0.getId()).get()).isEqualTo(userEntity0);
        assertThat(userRepository.findById(userEntity1.getId()).get()).isEqualTo(userEntity1);
    }

    @Test
    @DisplayName("BaseTimeEntity 등록")
    void baseTimeEntityCreate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        UserEntity user = setup.createUserEntity();

        //when
        testEntityManager.persist(user);

        //then
        UserEntity findUser = userRepository.findById(user.getId()).get();
        System.out.println("createdAt : " + user.getCreatedAt());
        System.out.println("modifiedAt : " + user.getModifiedAt());
        assertThat(findUser.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(findUser.getModifiedAt()).isBefore(LocalDateTime.now());
    }
}