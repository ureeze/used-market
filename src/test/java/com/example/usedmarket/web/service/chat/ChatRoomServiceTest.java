package com.example.usedmarket.web.service.chat;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserRepository userRepository;

    private Setup setup = new Setup();

    @Test
    void createChat() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        ChatRoomCreateRequestDto requestDto = ChatRoomCreateRequestDto.builder()
                .postId(3L)
                .build();

        //when
        ChatRoomResponseDto roomDto = chatRoomService.createChat(userPrincipal, requestDto);

        //then
        System.out.println(roomDto.toString());
    }
}