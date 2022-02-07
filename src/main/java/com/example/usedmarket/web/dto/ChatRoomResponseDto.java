package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chat.ChatRoom;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDto {

    private Long chatRoomId;

    private Long postId;

    private Long sellerId;

    private Long userId;

    private String userName;

    public static ChatRoomResponseDto toDto(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .postId(chatRoom.getPostId())
                .sellerId(chatRoom.getSellerId())
                .userId(chatRoom.getUserId())
                .userName(chatRoom.getUserName())
                .build();
    }
}