package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/chat/room")
    public ChatRoomResponseDto createChatRoom(@LoginUser UserPrincipal userPrincipal, @RequestBody ChatRoomCreateRequestDto requestDto) {
        return chatRoomService.createChat(userPrincipal, requestDto);
    }

    // 채팅방 생성
    @PostMapping("/chat/room2")
    public ChatRoomResponseDto createChatRoom2(@LoginUser UserPrincipal userPrincipal, @RequestBody ChatRoomCreateRequestDto requestDto) {
        return chatRoomService.createChat(userPrincipal, requestDto);
    }

    // sellerId 에 대한 채팅방리스트 조회
    @GetMapping("/chat/room/seller/{sellerId}")
    public List<ChatRoomResponseDto> getChatRoom(@PathVariable("sellerId") Long sellerId) {
        return chatRoomService.retrieveChatRoomListOfSeller(sellerId);
    }
}