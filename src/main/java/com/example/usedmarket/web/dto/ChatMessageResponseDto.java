package com.example.usedmarket.web.dto;

import com.example.usedmarket.web.domain.chatMessage.ChatMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponseDto implements Serializable {

    private Long chatMessageId;

    private Long chatRoomId;

    private Long userId;

    private String userName;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto toDto(ChatMessage chatMessage){
        return ChatMessageResponseDto.builder()
                .chatMessageId(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoomId())
                .userId(chatMessage.getUserEntity().getId())
                .userName(chatMessage.getUserEntity().getName())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}