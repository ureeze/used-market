package com.example.usedmarket.web.repository.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
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

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CREATE_AT", nullable = true)
    private LocalDateTime createAt;

    @Builder
    public Member(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }
}