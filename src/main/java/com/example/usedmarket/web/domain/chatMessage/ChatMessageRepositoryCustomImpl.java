package com.example.usedmarket.web.domain.chatMessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.usedmarket.web.domain.chatMessage.QChatMessage.chatMessage;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatMessage> findByChatRoomId(Long chatRoomId) {
        return jpaQueryFactory.selectFrom(chatMessage)
//                .leftJoin(chatMessage.userEntity, userEntity)
//                .fetchJoin()
                .where(chatMessage.chatRoomId.eq(chatRoomId))
                .orderBy(chatMessage.createdAt.asc())
                .fetch();
    }
}