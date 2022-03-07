package com.example.usedmarket.web.domain.chatMessage;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    // ChatRoomId 에 의한 ChatMessage List 조회
    List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
