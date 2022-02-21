package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chatMessage.ChatMessage;
import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequestDto implements Serializable {

    private Long chatRoomId;

    private Long userId;

    private String message;

    public ChatMessage createChatMessage(UserEntity userEntity) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .userEntity(userEntity)
                .message(message)
                .build();
    }
}