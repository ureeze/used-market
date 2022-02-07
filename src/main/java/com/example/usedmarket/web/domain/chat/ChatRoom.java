package com.example.usedmarket.web.domain.chat;


import com.example.usedmarket.web.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "CHATROOM")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private Long postId;

    @Column
    private Long sellerId;

    @Column
    private String sellerName;

    @Column
    private Long userId;

    @Column
    private String userName;
}