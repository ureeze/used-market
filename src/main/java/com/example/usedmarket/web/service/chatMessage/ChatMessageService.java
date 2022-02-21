package com.example.usedmarket.web.service.chatMessage;

import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.dto.ChatMessageResponseDto;

import java.util.List;

public interface ChatMessageService {
    // 메시지 발급
    void pubMessage(ChatMessageRequestDto requestDto);

    // 해당 ChatRoom 에 대한 ChatMessage 리스트 조회
    List<ChatMessageResponseDto> retrieveChatMessageByChatRoom(Long chatRoomId);
}