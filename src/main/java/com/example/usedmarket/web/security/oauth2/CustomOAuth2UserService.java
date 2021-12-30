package com.example.usedmarket.web.security.oauth2;

import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final HttpServletResponse response;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //OAuth2User
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //registrationId
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //attributes
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //nameAttributeKey
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //OAuth2UserInfo
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, attributes);

        //UserEntity
        UserEntity userEntity = userRepository.findByEmail(oAuth2UserInfo.getEmail()).map(entity -> entity.update(oAuth2UserInfo)).orElse(UserEntity.create(oAuth2UserInfo));

        userRepository.save(userEntity);
        log.info("Oauth2 UserEndPoint Save");
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity, oAuth2UserInfo);

        String token = tokenProvider.create(userEntity.getEmail());
        response.addHeader("Authorization", token);
        log.info("token : " + token);
        return userPrincipal;
    }
}
