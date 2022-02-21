package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomCreateRequestDto {

    private Long postId;

    public ChatRoom toChatRoom(UserPrincipal userPrincipal, UserEntity seller, Post post) {
        return ChatRoom.builder()
                .post(post)
                .userId(userPrincipal.getId())
                .userName(userPrincipal.getName())
                .sellerId(seller.getId())
                .sellerName(seller.getName())
                .build();
    }
}