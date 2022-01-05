package com.example.usedmarket.web.service.auth;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.LoginRequestDto;
import com.example.usedmarket.web.dto.SignUpRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;

    //회원 가입
    @Override
    public Long createUser(SignUpRequestDto signUpDto) {
        UserEntity userEntity = UserEntity.create(signUpDto, passwordEncoder);
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        return userPrincipal.getId();
    }


    //로그인
    @Override
    public String loginUser(LoginRequestDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String token = tokenProvider.create(userPrincipal.getEmail());
        response.addHeader("Authorization", token);
        log.info("token : " + token);
        return token;
    }
}
