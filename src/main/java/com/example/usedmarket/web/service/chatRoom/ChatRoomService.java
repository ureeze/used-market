package com.example.usedmarket.web.service.chatRoom;

import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomListResponseDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface ChatRoomService {
    // 채팅방 생성
    ChatRoomResponseDto createChat(UserPrincipal userPrincipal, Long postId);

    // sellerId 에 대한 채팅방리스트 조회
    List<ChatRoomResponseDto> retrieveChatRoomListOfSeller(Long sellerId);

//    // userId 와 sellerId 에 대한 ChatRoom 리스트 조회
//    List<ChatRoomResponseDto> retrieveChatRoomList(UserPrincipal userPrincipal);

    // userId 와 sellerId 에 대한 ChatRoom 리스트 조회
    ChatRoomListResponseDto retrieveChatRoomList(UserPrincipal userPrincipal);
}