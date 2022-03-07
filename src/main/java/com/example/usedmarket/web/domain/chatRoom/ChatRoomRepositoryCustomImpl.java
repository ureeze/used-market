package com.example.usedmarket.web.domain.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

import static com.example.usedmarket.web.domain.chatRoom.QChatRoom.chatRoom;

@Slf4j
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 사용자 ID 와 POST ID 에 의한 CHATROOM 조회
    @Override
    public Optional<ChatRoom> findByUserIdAndPostId(Long userId, Long postId) {
        log.info("FIND CHATROOM BY CHATROOM ID");
        return Optional.ofNullable(jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.userId.eq(userId), chatRoom.post.id.eq(postId))
                .fetchOne());
    }

    // 판매자에 의한 CHATROOM 조회
    @Override
    public List<ChatRoom> findBySellerId(Long sellerId) {
        log.info("FIND CHATROOM BY SELLER ID");
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.sellerId.eq(sellerId))
                .fetch();
    }

    // 구매자에 의한 CHATROOM 조회
    @Override
    public List<ChatRoom> findByBuyerId(Long buyerId) {
        log.info("FIND CHATROOM BY BUYER ID");
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.userId.eq(buyerId))
                .fetch();
    }
}