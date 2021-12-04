package com.example.usedmarket.web.domain.member;

import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.domain.BaseTimeEntity;
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

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public static Member toMember(SessionMember sessionMember) {
        //return new Member(sessionMember.getId(), sessionMember.getName(), sessionMember.getEmail(), sessionMember.getPicture(), Role.USER);
        return Member.builder()
                .id(sessionMember.getId())
                .name(sessionMember.getName())
                .email(sessionMember.getEmail())
                .picture(sessionMember.getPicture())
                .role(Role.USER)
                .build();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}