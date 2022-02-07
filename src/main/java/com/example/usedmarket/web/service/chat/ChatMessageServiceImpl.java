package com.example.usedmarket.web.service.chat;

import com.example.usedmarket.web.domain.chat.ChatMessage;
import com.example.usedmarket.web.domain.chat.ChatMessageRepository;
import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.dto.ChatMessageResponseDto;
import com.example.usedmarket.web.domain.chat.ChatRoomRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        log.info("send");

        // UserEntity 조회
        Optional<UserEntity> userEntity = userRepository.findById(requestDto.getUserId());
        if(!userEntity.isPresent()){
            // userEntity 존재하지 않음
            new UserNotFoundException("존재하지 않는 사용자 입니다.");
        }

        // ChatMessage 생성 및 DB 저장
        ChatMessage chatMessage = requestDto.createChatMessage(userEntity.get());
        chatMessageRepository.save(chatMessage);

        // ChatMessage 를 ChatMessageResponseDto 로 변환
        ChatMessageResponseDto responseDto = ChatMessageResponseDto.toDto(chatMessage);
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room.1", responseDto);
    }
}