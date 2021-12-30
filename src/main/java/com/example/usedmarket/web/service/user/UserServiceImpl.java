package com.example.usedmarket.web.service.user;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.*;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final HttpServletResponse response;


    /*
    UserRepository 에서 UserEntity 조회
    @param 찾고자 하는 UserEntity ID 값
    @return findUser 를 UserResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        // id 값을 이용해 UserRepository 에서 UserEntity 조회
        UserEntity findUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User 가 존재하지 않습니다."));

        // findUser 를 UserResponseDto 로 변환 후 반환
        return UserResponseDto.toDto(findUser);
    }

    /*
    UserRepository 에서 UserEntity 리스트 조회
    @return UserEntity 를 UserResponseDto 로 stream 을 이용해 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        // UserRepository 에서 UserEntity 전체 조회
        List<UserEntity> userList = userRepository.findAll();

        // Stream 을 이용해 UserResponseDto 타입으로 변환하여 반환
        return userList.stream().map(userEntity -> UserResponseDto.toDto(userEntity)).collect(Collectors.toList());
    }

    /*
    UserEntity 의 ID 값과 수정하고자 하는 UserUpdateRequestDto 를 Input 으로 받음
    @param ID - 찾고자 하는 UserEntity ID 값
    @param requestDto - 업데이트 하고자 하는 요청 정보
    @return 수정된 UserEntity 를 UserResponseDto 로 변환 후 반환
     */
    @Transactional
    public UserResponseDto updatePersonalInfo(UserPrincipal userPrincipal, UserUpdateRequestDto requestDto) {

        // ID 값을 이용해 UserRepository 에서 UserEntity 조회
        UserEntity user = userRepository.getById(userPrincipal.getId());
        // requestDto 를 이용해 UserEntity 수정
        user.update(requestDto);
        // user 가 영속성 컨텍스트에 존재하기 때문에 바로 UserResponseDto 로 반환
        return UserResponseDto.toDto(user);
    }

    /*
     * 회원 탈퇴
     * Input 으로 들어온 Id 값에 해당하는 UserEntity 제거
     * @param id - 삭제하고자 하는 UserEntity 의 ID
     */
    @Transactional
    public void delete(UserPrincipal userPrincipal, Long id) throws IOException {
        // UserRepository 에서 ID 에 맞는 UserEntity 삭제
        userRepository.deleteById(id);
        // SecurityContextHolder 에서 context 삭제
        SecurityContextHolder.clearContext();

        response.sendRedirect("/main");
    }
}
