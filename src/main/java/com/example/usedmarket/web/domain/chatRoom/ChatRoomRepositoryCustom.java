package com.example.usedmarket.web.domain.chatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findByUserIdOrSellerId(Long userId);
    Optional<ChatRoom> findByUserIdAndPostId(Long userId, Long postId);
    List<ChatRoom> findBySellerId(Long sellerId);
    List<ChatRoom> findByBuyerId(Long buyerId);

}
