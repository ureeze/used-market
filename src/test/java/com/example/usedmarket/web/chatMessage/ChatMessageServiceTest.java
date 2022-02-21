package com.example.usedmarket.web.chatMessage;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.chatMessage.ChatMessage;
import com.example.usedmarket.web.domain.chatMessage.ChatMessageRepository;
import com.example.usedmarket.web.domain.chatRoom.ChatRoomRepository;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.ChatMessageRequestDto;
import com.example.usedmarket.web.dto.ChatMessageResponseDto;
import com.example.usedmarket.web.service.chatMessage.ChatMessageService;
import com.example.usedmarket.web.service.chatRoom.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ChatMessageServiceTest {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private Setup setup = new Setup();

    @Test
    @DisplayName("메시지 발급")
    void pubMessage() {
        // given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);

        String message = "message 전송";
        ChatMessageRequestDto requestDto = new ChatMessageRequestDto(1L, userEntity.getId(), message);

        // when
        chatMessageService.pubMessage(requestDto);

        // then
    }

    @Test
    @DisplayName("해당 ChatRoom 에 대한 ChatMessage 리스트 조회")
    void retrieveChatMessageByChatRoom() {
        // given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);

        Long chatRoomId = 1L;
        String message = "message 전송";
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message)
                .chatRoomId(chatRoomId)
                .userEntity(userEntity)
                .build();
        chatMessageRepository.save(chatMessage);

        // when
        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageService.retrieveChatMessageByChatRoom(chatRoomId);

        // then
        assertThat(chatMessageResponseDtoList.get(0).getChatRoomId()).isEqualTo(chatRoomId);
        assertThat(chatMessageResponseDtoList.get(0).getUserId()).isEqualTo(userEntity.getId());
        assertThat(chatMessageResponseDtoList.get(0).getMessage()).isEqualTo(message);

    }
}
