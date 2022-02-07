package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chat.ChatRoom;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomCreateRequestDto {

    private Long postId;
    private Long sellerId;

    public ChatRoom toChatRoom(UserPrincipal userPrincipal, UserEntity seller) {
        return ChatRoom.builder()
                .postId(this.postId)
                .userId(userPrincipal.getId())
                .userName(userPrincipal.getName())
                .sellerId(seller.getId())
                .sellerName(seller.getName())
                .build();
    }
}