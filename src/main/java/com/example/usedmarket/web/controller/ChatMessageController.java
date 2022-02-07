package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.service.chat.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void chatting(ChatMessageRequestDto requestDto) throws Exception {
        chatMessageService.pubMessage(requestDto);
    }
}