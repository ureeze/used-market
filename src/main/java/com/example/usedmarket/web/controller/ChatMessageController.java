package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.dto.ChatMessageResponseDto;
import com.example.usedmarket.web.service.chatMessage.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    // ChatMessage 전송
    @MessageMapping("/chat")
    public void chatting(ChatMessageRequestDto requestDto) {
        log.info("CHAT SEND");
        System.out.println(requestDto);
        chatMessageService.pubMessage(requestDto);
    }

    // 해당 ChatRoom 에 대한 ChatMessage 리스트 조회
    @GetMapping("/chatroom/{chatRoomId}/messages")
    public List<ChatMessageResponseDto> getChatMessageListByChatRoomId(@PathVariable Long chatRoomId) {
       return chatMessageService.retrieveChatMessageByChatRoom(chatRoomId);
    }
}