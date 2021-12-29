package com.example.usedmarket.web.security.oauth2;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

        if (registrationId.equals("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new IllegalStateException();
        }
    }
}