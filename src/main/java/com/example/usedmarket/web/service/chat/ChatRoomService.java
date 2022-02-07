package com.example.usedmarket.web.service.chat;

import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface ChatRoomService {
    // 채팅방 생성
    ChatRoomResponseDto createChat(UserPrincipal userPrincipal, ChatRoomCreateRequestDto requestDto);

    // sellerId 에 대한 채팅방리스트 조회
    List<ChatRoomResponseDto> retrieveChatRoomListOfSeller(Long sellerId);
}