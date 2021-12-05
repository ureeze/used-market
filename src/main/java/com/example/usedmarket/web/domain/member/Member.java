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

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 20)
    private String name;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PICTURE", length = 2000)
    private String picture;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Member update(MemberRequestDto requestDto) {
        update(requestDto.getName(), requestDto.getEmail());
        return this;
    }

    public Member update(OAuthAttributes attributes) {
        update(attributes.getName(), attributes.getEmail());
        return this;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }


    public String getRoleKey() {
        return role.getKey();
    }
}