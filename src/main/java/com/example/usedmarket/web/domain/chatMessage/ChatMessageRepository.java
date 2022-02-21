package com.example.usedmarket.web.domain.chatMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> , ChatMessageRepositoryCustom {

}