package com.example.usedmarket.web.domain.chatRoom;


import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.post.Post;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column
    private Long sellerId;

    @Column
    private String sellerName;

    @Column
    private Long userId;

    @Column
    private String userName;
}