package com.example.usedmarket.web.domain.user;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;

    /*
    FormLogin 용 UserEntity 생성
     */
    UserEntity createUserEntity() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "PBJ" + num;
        String email = name + "@google.com";
        String password = num + "";

        UserEntity userEntity = UserEntity.builder()
                .name(name).password(password)
                .email(email).picture(null).registrationId(null)
                .role(Role.USER)
                .build();
        return userEntity;
    }

    /*
    OAuth2.0 용 UserEntity 생성
     */
    UserEntity createUserEntity_OAuth2() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "PBJ" + num;
        String email = name + "@google.com";
        String registrationId = "google";

        UserEntity userEntity = UserEntity.builder()
                .name(name).password(null)
                .email(email).picture(num + "").registrationId(registrationId)
                .role(Role.USER)
                .build();
        return userEntity;
    }

    @Test
    @DisplayName("저장 테스트")
    void saveUserTest() {
        //given
        UserEntity userEntity = createUserEntity();

        //when
        testEntityManager.persist(userEntity);

        //then
        assertThat(userRepository.findById(userEntity.getId()).get()).isEqualTo(userEntity);
    }


    @Test
    @DisplayName("조회 테스트")
    void findMemberTest() {
        //given
        UserEntity userEntity1 = createUserEntity();
        UserEntity userEntity2 = createUserEntity();

        //when
        testEntityManager.persist(userEntity1);
        testEntityManager.persist(userEntity2);

        //then
        assertThat(userRepository.findById(userEntity1.getId()).get()).isEqualTo(userEntity1);
        assertThat(userRepository.findById(userEntity2.getId()).get()).isEqualTo(userEntity2);
    }

    @Test
    @DisplayName("BaseTimeEntity 등록")
    void baseTimeEntityCreate() {
        //given
        LocalDateTime now = LocalDateTime.now();
        now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        UserEntity user = createUserEntity();

        //when
        testEntityManager.persist(user);

        //then
        UserEntity findUser = userRepository.findById(user.getId()).get();
        System.out.println("createdAt : " + user.getCreatedAt());
        System.out.println("modifiedAt : " + user.getModifiedAt());
//        assertThat(findUser.getCreatedAt()).isAfter(now);
//        assertThat(findUser.getModifiedAt()).isAfter(now);
    }
}