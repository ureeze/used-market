package com.example.usedmarket.web.service.chat;

import com.example.usedmarket.web.domain.chat.ChatRoom;
import com.example.usedmarket.web.domain.chat.ChatRoomRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.exception.ChatRoomNotCreatedException;
import com.example.usedmarket.web.exception.PostNotFoundException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final PostRepository postRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성
    @Override
    public ChatRoomResponseDto createChat(@LoginUser UserPrincipal userPrincipal, ChatRoomCreateRequestDto requestDto) {
        // POST 를 통한 seller 조회
        Optional<Post> post = postRepository.findByPostId(requestDto.getPostId());
        if(!post.isPresent()){
            new PostNotFoundException("POST 가 존재하지 않습니다.");
        }

        UserEntity seller = post.get().getUserEntity();
        if (seller == null) {
            new UserNotFoundException("판매자가 존재하지 않습니다.");
        }

        // 채팅생성요청 USER 가 Seller 와 같은 경우 예외처리
        if (userPrincipal.getId() == seller.getId()) {
            new ChatRoomNotCreatedException("POST 작성자는 채팅방을 생성 할 수 없습니다.");
        }

        // 채팅방 존재 여부 확인
        Optional<ChatRoom> checkChatRoom = chatRoomRepository.findByUserIdAndSellerIdAndPostId(userPrincipal.getId(), seller.getId(), requestDto.getPostId());
        if (checkChatRoom.isPresent()) {
            // 채팅방 존재 할 경우 기존 채팅방 반환
            log.info("채팅방 존재");
            return ChatRoomResponseDto.toDto(checkChatRoom.get());
        } else {
            // 채팅방 존재하지 않을 경우 채팅방 생성
            log.info("채팅방 생성");
            ChatRoom newChatRoom = requestDto.toChatRoom(userPrincipal, seller);
            chatRoomRepository.save(newChatRoom);
            return ChatRoomResponseDto.toDto(newChatRoom);
        }
    }

    // sellerId 의 특정 POST 에 대한 ChatRoomList 조회
    @Override
    public List<ChatRoomResponseDto> retrieveChatRoomListOfSeller(Long sellerId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findBySellerId(sellerId);
        return chatRoomList.stream().map(chatRoom -> ChatRoomResponseDto.toDto(chatRoom)).collect(Collectors.toList());
    }
}