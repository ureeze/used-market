package com.example.usedmarket.web.service;

import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.SignUpDto;
import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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


    UserUpdateRequestDto createUserUpdateRequestDto() {
        return UserUpdateRequestDto.builder()
                .userName("park")
                .build();
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

    @Test
    @DisplayName("본인 정보 조회")
    void getCurrentUser() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);

        //when
        UserResponseDto responseDto = userService.getCurrentUser(userPrincipal);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(responseDto.getUserId()).isEqualTo(userEntity.getId());
    }


    @Test
    @DisplayName("USER ID 를 이용한 사용자 조회 (ADMIN)")
    void findUserEntityTest() {
        //given
        UserEntity user = createUserEntity();
        userRepository.save(user);

        //when
        UserResponseDto responseDto = userService.findByUserId(user.getId());

        //then
        assertThat(responseDto.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("전체 USER 목록 조회 (ADMIN)")
    void findAllUserEntityTest() {
        //given
        UserEntity user1 = createUserEntity();
        UserEntity user2 = createUserEntity();
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<UserResponseDto> responseDtoList = userService.findAll();

        //then
        assertThat(responseDtoList.get(0).getUserId()).isEqualTo(user1.getId());
        assertThat(responseDtoList.get(1).getUserId()).isEqualTo(user2.getId());
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void updatePersonalInfo() {
        //given
        UserEntity userEntity = createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        UserUpdateRequestDto userUpdateRequestDto = createUserUpdateRequestDto();

        //when
        UserResponseDto responseDto = userService.updatePersonalInfo(userPrincipal, userUpdateRequestDto);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(userPrincipal.getEmail());
        assertThat(responseDto.getUserName()).isEqualTo(userUpdateRequestDto.getUserName());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUserEntityTest() throws IOException {
        //given
        UserEntity user = createUserEntity();
        userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(user);

        //when
        userService.delete(userPrincipal);

        //then
    }
}