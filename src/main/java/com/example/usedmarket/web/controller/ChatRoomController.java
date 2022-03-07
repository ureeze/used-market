package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.ChatRoomListResponseDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.chatRoom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성
    @PostMapping("/chatrooms/post/{postId}")
    public ChatRoomResponseDto createChatRoom(@LoginUser UserPrincipal userPrincipal, @PathVariable("postId") Long postId) {
        log.info("채팅방 생성");
        return chatRoomService.createChat(userPrincipal, postId);
    }

    // sellerId 에 대한 ChatRoom List 조회
    @GetMapping("/chat/room/seller/{sellerId}")
    public List<ChatRoomResponseDto> getChatRoom(@PathVariable("sellerId") Long sellerId) {
        log.info("sellerId 에 대한 ChatRoom List 조회");
        return chatRoomService.retrieveChatRoomListOfSeller(sellerId);
    }

    // 구매와 판매 ChatRoom List 조회
    @GetMapping("/chatrooms")
    public ChatRoomListResponseDto getChatRoomList(@LoginUser UserPrincipal userPrincipal) {
        log.info("구매와 판매 ChatRoom List 조회");
        return chatRoomService.retrieveChatRoomList(userPrincipal);
    }
}