package com.example.usedmarket.web.service.auth;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.exception.PassWordNotMatchException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final HttpServletResponse response;


    @Override
    public Long createUser(SignUpDto signUpDto) {
        UserEntity user = UserEntity.create(signUpDto, passwordEncoder);
        userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(user);
        return userPrincipal.getId();
    }

    @Override
    public String loginUser(LoginRequestDto loginDto) {
        UserEntity user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("가입되지 않은 유저입니다."));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new PassWordNotMatchException("잘못된 비밀번호입니다.");
        }
        log.info("가입 회원입니다.");

        String token = tokenProvider.create(user);
        response.addHeader("Authorization", token);
        log.info("token : " + token);
        return token;
    }

}
