package com.example.usedmarket.web.domain.chatMessage;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
