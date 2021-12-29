package com.example.usedmarket.web.service.user;

import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.SignUpDto;
import com.example.usedmarket.web.dto.UserResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    SignUpDto createSignUpDto() {
        int num = (int) (Math.random() * 10000) + 1;

        String name = "PBJ" + num;
        String email = name + "@google.com";
        SignUpDto requestDto = SignUpDto.builder()
                .name(name)
                .email(email)
                .password("num")
                .build();
        return requestDto;
    }

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

    @AfterEach
    void clean() {
        userRepository.deleteAll();
    }

//    @Test
//    @DisplayName("service - USER 생성 테스트")
//    void saveMemberTest() {
//        //given
//        SignUpDto dto = createSignUpDto();
//
//        //when
//        Long id = userService.createUser(dto);
//
//        //then
//        assertThat(id).isGreaterThanOrEqualTo(0L);
//    }

    @Test
    @DisplayName("service - USER 조회 테스트")
    void findMemberTest() {
        //given
        UserEntity user = createUserEntity();
        userRepository.save(user);
        //when
        UserResponseDto responseDto = userService.findById(user.getId());

        //then
        assertThat(responseDto.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("service - 멤버 전체조회 테스트")
    void findAllMemberTest() {
        //given
        UserEntity user1 = createUserEntity();
        UserEntity user2 = createUserEntity();
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<UserResponseDto> responseDtoList = userService.findAll();

        //then
        assertThat(responseDtoList.get(0).getId()).isEqualTo(user1.getId());
        assertThat(responseDtoList.get(1).getId()).isEqualTo(user2.getId());
    }
}