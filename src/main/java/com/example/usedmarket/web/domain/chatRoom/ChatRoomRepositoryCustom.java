package com.example.usedmarket.web.domain.chatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    // 사용자 ID 와 POST ID 에 의한 CHATROOM 조회
    Optional<ChatRoom> findByUserIdAndPostId(Long userId, Long postId);

    // 판매자에 의한 CHATROOM 조회
    List<ChatRoom> findBySellerId(Long sellerId);

    // 구매자에 의한 CHATROOM 조회
    List<ChatRoom> findByBuyerId(Long buyerId);

}
