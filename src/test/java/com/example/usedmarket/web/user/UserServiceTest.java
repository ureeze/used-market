package com.example.usedmarket.web.user;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.UserResponseDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Setup setup = new Setup();

    //    @AfterEach
//    void clean() {
//        userRepository.deleteAll();
//    }

    private UserEntity userEntity;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
    }

    @Test
    @DisplayName("본인 정보 조회")
    void getCurrentUser() {
        //given

        //when
        entityManager.clear();
        UserResponseDto responseDto = userService.getCurrentUser(userPrincipal);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(userEntity.getEmail());
        assertThat(responseDto.getUserId()).isEqualTo(userEntity.getId());
    }


    @Test
    @DisplayName("USER ID 를 이용한 사용자 조회 (ADMIN)")
    void findUserEntityTest() {
        //given

        //when
        entityManager.clear();
        UserResponseDto responseDto = userService.findByUserId(userEntity.getId());

        //then
        assertThat(responseDto.getUserId()).isEqualTo(userEntity.getId());
    }

    @Test
    @DisplayName("전체 USER 목록 조회 (ADMIN)")
    void findAllUserEntityTest() {
        //given
        UserEntity userEntity1 = setup.createUserEntity();

        userRepository.save(userEntity1);

        //when
        entityManager.clear();
        List<UserResponseDto> responseDtoList = userService.findAll();

        //then
        assertThat(responseDtoList.get(0).getUserId()).isEqualTo(userEntity.getId());
        assertThat(responseDtoList.get(1).getUserId()).isEqualTo(userEntity1.getId());
    }

    @Test
    @DisplayName("사용자 정보 수정")
    void updatePersonalInfo() {
        //given
        UserUpdateRequestDto requestDto = setup.createUserUpdateRequestDto();

        //when
        entityManager.clear();
        UserResponseDto responseDto = userService.updatePersonalInfo(userPrincipal, requestDto);

        //then
        assertThat(responseDto.getEmail()).isEqualTo(userPrincipal.getEmail());
        assertThat(responseDto.getUserName()).isEqualTo(requestDto.getUserName());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteUserEntityTest() throws IOException {
        //given

        //when
        entityManager.clear();
        userService.delete(userPrincipal);

        //then
    }
}