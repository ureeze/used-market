package com.example.usedmarket.web.domain.member;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.security.dto.OAuthAttributes;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseTimeEntity {

    //Member ID
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //사용자 이름
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    //사용자 이메일
    @Column(name = "EMAIL", nullable = false)
    private String email;

    //사용자 프로필사진
    @Column(name = "PICTURE", length = 2000)
    private String picture;

    //사용자 권한
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Member) {
            Member member = (Member) obj;
            if (id.equals(member.id)) {
                return true;
            }
        }
        return false;
    }

    // 회원정보 수정
    public Member update(MemberRequestDto requestDto) {
        update(requestDto.getName());
        return this;
    }

    // 회원정보 수정
    public Member update(OAuthAttributes attributes) {
        this.name = attributes.getName();
        this.email = attributes.getEmail();
        return this;
    }

    // 회원정보 수정
    public void update(String name) {
        this.name = name;
    }

    // 사용자의 권한 반환
    public String getRoleKey() {
        return role.getKey();
    }
}