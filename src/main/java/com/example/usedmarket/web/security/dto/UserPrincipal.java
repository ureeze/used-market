package com.example.usedmarket.web.security.dto;


import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.security.oauth2.OAuth2UserInfo;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPrincipal implements UserDetails, OAuth2User, Serializable {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String picture;
    private Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;


    public static UserPrincipal createUserPrincipal(UserEntity userEntity) {
        return UserPrincipal.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .picture(userEntity.getPicture())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(Role.USER.name())))
                .build();
    }

    public static UserPrincipal createUserPrincipal(UserEntity userEntity, OAuth2UserInfo oAuth2UserInfo) {
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        userPrincipal.setAttributes(oAuth2UserInfo.getAttributes());
        return userPrincipal;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
