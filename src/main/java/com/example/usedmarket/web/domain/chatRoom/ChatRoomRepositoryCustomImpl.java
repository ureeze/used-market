package com.example.usedmarket.web.domain.chatRoom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.usedmarket.web.domain.chatRoom.QChatRoom.chatRoom;

@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoom> findByUserIdOrSellerId(Long userId) {
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.userId.eq(userId).or(chatRoom.sellerId.eq(userId)))
                .orderBy(chatRoom.createdAt.asc())
                .fetch();
    }

    @Override
    public Optional<ChatRoom> findByUserIdAndPostId(Long userId, Long postId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.userId.eq(userId), chatRoom.post.id.eq(postId))
                .fetchOne());
    }

    @Override
    public List<ChatRoom> findBySellerId(Long sellerId) {
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.sellerId.eq(sellerId))
                .fetch();
    }

    @Override
    public List<ChatRoom> findByBuyerId(Long buyerId) {
        return jpaQueryFactory.selectFrom(chatRoom)
                .where(chatRoom.userId.eq(buyerId))
                .fetch();
    }
}