package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chat.ChatMessage;
import com.example.usedmarket.web.domain.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto implements Serializable {

    private Long chatRoomId;

    private Long userId;

    private String userName;

    private String message;

    public ChatMessage createChatMessage(UserEntity userEntity) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .userEntity(userEntity)
                .message(message)
                .build();
    }
}