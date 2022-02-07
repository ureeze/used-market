package com.example.usedmarket.web.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomRepositoryCustom {
    Optional<ChatRoom> findByUserIdAndSellerIdAndPostId(Long userId, Long sellerId, Long postId);
    List<ChatRoom> findBySellerId(Long sellerId);
}