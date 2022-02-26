package com.example.usedmarket.web.service.chatRoom;

import com.example.usedmarket.web.dto.ChatRoomListResponseDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface ChatRoomService {
    // 채팅방 생성
    ChatRoomResponseDto createChat(UserPrincipal userPrincipal, Long postId);

    // sellerId 에 대한 채팅방리스트 조회
    List<ChatRoomResponseDto> retrieveChatRoomListOfSeller(Long sellerId);

    // 구매하는 경우와 판매하려는 경우에 대한 ChatRoom 리스트 조회
    ChatRoomListResponseDto retrieveChatRoomList(UserPrincipal userPrincipal);
}