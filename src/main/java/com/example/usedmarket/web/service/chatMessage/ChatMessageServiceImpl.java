package com.example.usedmarket.web.service.chatMessage;

import com.example.usedmarket.web.domain.chatMessage.ChatMessage;
import com.example.usedmarket.web.domain.chatMessage.ChatMessageRepository;
import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.dto.ChatMessageResponseDto;
import com.example.usedmarket.web.domain.chatRoom.ChatRoomRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.exception.ChatRoomNotFoundException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final static String CHAT_EXCHANGE_NAME = "amq.topic";
    private final static String CHAT_QUEUE_NAME = "chatqueue";
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    // 채팅 전송
    @Override
    public void pubMessage(ChatMessageRequestDto requestDto) {
        log.info("PUB MESSAGE");

        // UserEntity 조회
        Optional<UserEntity> userEntity = userRepository.findById(requestDto.getUserId());
        if (!userEntity.isPresent()) {
            // userEntity 존재하지 않음
            new UserNotFoundException("존재하지 않는 사용자 입니다.");
        }

        // ChatMessage 생성 및 DB 저장
        ChatMessage chatMessage = requestDto.createChatMessage(userEntity.get());
        chatMessageRepository.save(chatMessage);

        // ChatMessage 를 ChatMessageResponseDto 로 변환 후 exchange 로 전송 (routingKey : room.chatRoomId)
        ChatMessageResponseDto responseDto = ChatMessageResponseDto.toDto(chatMessage);
        String routingKey = "room." + requestDto.getChatRoomId();
        log.info(routingKey);

        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, routingKey, responseDto);
    }

    // 해당 ChatRoom 에 대한 ChatMessage 리스트 조회
    @Override
    public List<ChatMessageResponseDto> retrieveChatMessageByChatRoom(Long chatRoomId) {

        // ChatRoom 검색
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatRoomId);
        if (!chatRoom.isPresent()) {
            new ChatRoomNotFoundException("채팅방이 존재하지 않습니다.");
        }

        // ChatRoom 에 대한 ChatMessage 조회
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoomId(chatRoomId);
        return chatMessageList.stream().map(chatMessage -> ChatMessageResponseDto.toDto(chatMessage)).collect(Collectors.toList());
    }
}