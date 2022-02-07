package com.example.usedmarket.web.service.chat;

import com.example.usedmarket.web.dto.ChatMessageRequestDto;

public interface ChatMessageService {
    void pubMessage(ChatMessageRequestDto requestDto);
}