package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDto implements Serializable {

    private Long chatRoomId;

    private Long postId;

    private String postTitle;

    private Long sellerId;

    private Long userId;

    private String userName;

    public static ChatRoomResponseDto toDto(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
                .chatRoomId(chatRoom.getId())
                .postId(chatRoom.getPost().getId())
                .postTitle(chatRoom.getPost().getTitle())
                .sellerId(chatRoom.getSellerId())
                .userId(chatRoom.getUserId())
                .userName(chatRoom.getUserName())
                .build();
    }
}