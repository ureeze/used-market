package com.example.usedmarket.web.service.chatRoom;

import com.example.usedmarket.web.domain.chatMessage.ChatMessage;
import com.example.usedmarket.web.domain.chatMessage.ChatMessageRepository;
import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import com.example.usedmarket.web.domain.chatRoom.ChatRoomRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomListResponseDto;
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
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성
    @Override
    public ChatRoomResponseDto createChat(@LoginUser UserPrincipal userPrincipal, Long postId) {
        // POST 를 통한 seller 조회
        Optional<Post> post = postRepository.findByPostId(postId);
        if (!post.isPresent()) {
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

        // ChatRoom 존재 여부 확인
        Optional<ChatRoom> checkChatRoom = chatRoomRepository.findByUserIdAndPostId(userPrincipal.getId(), postId);
        if (checkChatRoom.isPresent()) {

            // 채팅방 존재 할 경우 저장된 ChatMessage List 와 함께 기존 ChatRoom 반환
            log.info("ChatRoom 존재");
            ChatRoom chatRoom = checkChatRoom.get();
            log.info("ChatRoomId : " + chatRoom.getId());

            // 저장된 ChatMessage List 조회
            List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoom.getId());

            // 기존 ChatRoom 반환
            return ChatRoomResponseDto.toDto(chatRoom);
        } else {
            // ChatRoom 존재하지 않을 경우 새로운 ChatRoom 생성
            log.info("ChatRoom 생성");

            ChatRoom newChatRoom = ChatRoom.builder()
                    .post(post.get())
                    .userId(userPrincipal.getId())
                    .userName(userPrincipal.getName())
                    .sellerId(seller.getId())
                    .sellerName(seller.getName())
                    .build();

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


    // 구매하는 경우와 판매하려는 경우에 대한 ChatRoom 리스트 조회
    @Override
    public ChatRoomListResponseDto retrieveChatRoomList(@LoginUser UserPrincipal userPrincipal) {
        // USER 가 SELLER 인 경우 ChatRoom 조회
        List<ChatRoomResponseDto> chatRoomListBySeller = chatRoomRepository.findBySellerId(userPrincipal.getId())
                .stream().map(chatRoom -> ChatRoomResponseDto.toDto(chatRoom))
                .collect(Collectors.toList());

        // USER 가 BUYER 인 경우 ChatRoom 조회
        List<ChatRoomResponseDto> chatRoomListByBuyer = chatRoomRepository.findByBuyerId(userPrincipal.getId())
                .stream().map(chatRoom -> ChatRoomResponseDto.toDto(chatRoom))
                .collect(Collectors.toList());

        return ChatRoomListResponseDto.builder()
                .chatRoomListBySeller(chatRoomListBySeller)
                .chatRoomListByBuyer(chatRoomListByBuyer)
                .build();
    }
}