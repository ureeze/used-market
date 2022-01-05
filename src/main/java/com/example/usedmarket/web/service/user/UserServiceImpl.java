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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /*
    본인 정보 조회
    @param userPrincipal - 현재 사용자
    @return findUser 를 UserResponseDto 로 변환 후 반환
     */
    @Cacheable(key = "'user-'+#userPrincipal.id", value = "user")
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser(@LoginUser UserPrincipal userPrincipal) {
        // email 값을 이용해 UserRepository 에서 UserEntity 조회
        UserEntity findUser = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("User 가 존재하지 않습니다."));

        // findUser 를 UserResponseDto 로 변환 후 반환
        return UserResponseDto.toDto(findUser);
    }

    /*
    USER ID 를 이용한 사용자 조회 (ADMIN)
    @param 찾고자 하는 UserEntity ID 값
    @return findUser 를 UserResponseDto 로 변환 후 반환
     */
    @Cacheable(key = "'user-'+#userId", value = "user")
    @Transactional(readOnly = true)
    public UserResponseDto findByUserId(Long userId) {
        // userId 값을 이용해 UserRepository 에서 UserEntity 조회
        UserEntity findUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User 가 존재하지 않습니다."));

        // findUser 를 UserResponseDto 로 변환 후 반환
        return UserResponseDto.toDto(findUser);
    }

    /*
    전체 USER 목록 조회 (ADMIN)
    @return UserEntity 를 UserResponseDto 로 stream 을 이용해 변환 후 리스트로 반환
     */
    @Cacheable(key = "'userAll'", value = "userAll")
    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        // UserRepository 에서 UserEntity 전체 조회
        List<UserEntity> userList = userRepository.findAll();

        // Stream 을 이용해 UserResponseDto 타입으로 변환하여 반환
        return userList.stream().map(userEntity -> UserResponseDto.toDto(userEntity)).collect(Collectors.toList());
    }

    /*
    사용자 정보 수정
    @param userPrincipal - 현재 사용자
    @param requestDto - 업데이트 하고자 하는 요청 정보
    @return 수정된 UserEntity 를 UserResponseDto 로 변환 후 반환
     */
    @Caching(evict = {
            //USER 개인 상세 정보
            @CacheEvict(key = "'user-'+#userPrincipal.id", value = "user"),
            //USER 전체 리스트
            @CacheEvict(key = "'userAll'", value = "userAll")
    })
    @Transactional
    public UserResponseDto updatePersonalInfo(@LoginUser UserPrincipal userPrincipal, UserUpdateRequestDto requestDto) {

        // ID 값을 이용해 UserRepository 에서 UserEntity 조회
        UserEntity user = userRepository.getById(userPrincipal.getId());

        // requestDto 를 이용해 UserEntity 수정
        user.update(requestDto);

        // user 가 영속성 컨텍스트에 존재하기 때문에 바로 UserResponseDto 로 반환
        return UserResponseDto.toDto(user);
    }

    /*
     * 회원 탈퇴
     * @param userPrincipal - 현재 사용자
     */
    @Caching(evict = {
            //USER 개인 상세 정보
            @CacheEvict(key = "'user-'+#userPrincipal.id", value = "user"),
            //USER 전체 리스트
            @CacheEvict(key = "'userAll'", value = "userAll")
    })
    @Transactional
    public void delete(@LoginUser UserPrincipal userPrincipal) throws IOException {
        // Repository 에서 UserEntity 삭제
        userRepository.deleteById(userPrincipal.getId());

        // SecurityContextHolder 에서 context 삭제
        SecurityContextHolder.clearContext();
    }
}
