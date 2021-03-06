package com.example.usedmarket.web.domain.user;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.dto.SignUpRequestDto;
import com.example.usedmarket.web.dto.UserUpdateRequestDto;
import com.example.usedmarket.web.security.oauth2.OAuth2UserInfo;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Getter
@Entity
@Builder
@Table(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseTimeEntity implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String picture;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String registrationId;

    /*
    OAuth2 가입
     */
    public static UserEntity create(OAuth2UserInfo oAuth2UserInfo) {
        return UserEntity.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .picture(oAuth2UserInfo.getImageUrl())
                .password(null)
                .role(Role.USER)
                .registrationId(null)
                .build();
    }


    /*
    ID/PW 가입
     */
    public static UserEntity create(SignUpRequestDto signUpDto, PasswordEncoder passwordEncoder) {
        String pw = passwordEncoder.encode(signUpDto.getPassword());
        return UserEntity.builder()
                .name(signUpDto.getUserName())
                .email(signUpDto.getEmail())
                .password(pw)
                .picture(null)
                .role(Role.USER)
                .registrationId(null)
                .build();
    }

    /*
    OAuth2.0 로그인 업데이트
     */
    public UserEntity update(OAuth2UserInfo oAuth2UserInfo) {
        this.name = oAuth2UserInfo.getName();
        this.email = oAuth2UserInfo.getEmail();
        this.picture = oAuth2UserInfo.toString();
        return this;
    }

    /*
    회원정보수정
     */
    public UserEntity update(UserUpdateRequestDto requestDto) {
        this.name = requestDto.getUserName();
//        this.email = requestDto.getEmail();
        return this;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) obj;
            if (email.equals(userEntity.getEmail()) && id == userEntity.getId()) {
                return true;
            }
        }
        return false;
    }
}