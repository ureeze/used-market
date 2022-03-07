package com.example.usedmarket.web.domain.chatMessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.example.usedmarket.web.domain.chatMessage.QChatMessage.chatMessage;
import static com.example.usedmarket.web.domain.user.QUserEntity.userEntity;

@Slf4j
@RequiredArgsConstructor
public class ChatMessageRepositoryCustomImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    // ChatRoomId 에 의한 ChatMessage List 조회
    public List<ChatMessage> findByChatRoomId(Long chatRoomId) {
        log.info("FIND CHAT MESSAGE BY CHATROOM ID");
        return queryFactory.selectFrom(chatMessage)
//                .leftJoin(chatMessage.userEntity, userEntity)
//                .fetchJoin()
                .where(chatMessage.chatRoomId.eq(chatRoomId))
                .orderBy(chatMessage.createdAt.asc())
                .fetch();
    }
}