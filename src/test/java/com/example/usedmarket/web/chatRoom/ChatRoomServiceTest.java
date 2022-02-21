package com.example.usedmarket.web.chatRoom;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import com.example.usedmarket.web.domain.chatRoom.ChatRoomRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.ChatRoomCreateRequestDto;
import com.example.usedmarket.web.dto.ChatRoomResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.chatRoom.ChatRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;


    private Setup setup = new Setup();

    @Test
    @DisplayName("채팅방 생성")
    void createChat() {
        // given
        // UserEntity, UserPrincipal
        UserEntity seller = setup.createUserEntity();
        UserEntity buyer = setup.createUserEntity();
        userRepository.saveAll(Arrays.asList(buyer, seller));
        UserPrincipal buyerPrincipal = UserPrincipal.createUserPrincipal(buyer);

        // Post
        Post post = setup.createPost(seller);
        postRepository.save(post);

//        // ChatRoomCreateRequestDto
//        ChatRoomCreateRequestDto requestDto = ChatRoomCreateRequestDto.builder()
//                .postId(post.getId())
//                .build();

        //when
        ChatRoomResponseDto responseDto = chatRoomService.createChat(buyerPrincipal, post.getId());

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(post.getTitle());
        assertThat(responseDto.getSellerId()).isEqualTo(seller.getId());
    }

    @Test
    @DisplayName("sellerId 의 특정 Post 에 대한 ChatRoomList 조회")
    void retrieveChatRoomListOfSeller() {
        // given
        // UserEntity, UserPrincipal
        UserEntity seller = setup.createUserEntity();
        userRepository.save(seller);

        // Post
        Post post = setup.createPost(seller);
        postRepository.save(post);

        // ChatRoom
        ChatRoom chatRoom = ChatRoom.builder()
                .sellerId(seller.getId())
                .post(post)
                .sellerName(seller.getName())
                .build();

        chatRoomRepository.save(chatRoom);

        // when
        List<ChatRoomResponseDto> responseDtoList = chatRoomService.retrieveChatRoomListOfSeller(post.getUserEntity().getId());

        // then
        assertThat(responseDtoList.get(0).getSellerId()).isEqualTo(seller.getId());
        assertThat(responseDtoList.get(0).getPostId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("userId 와 sellerId 에 대한 ChatRoom 리스트 조회")
    void retrieveChatRoomList() {
        // given
        // UserEntity, UserPrincipal
        UserEntity user = setup.createUserEntity();
        userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(user);

        // Post
        Post post = setup.createPost(user);
        postRepository.save(post);

        // ChatRoom
        ChatRoom chatRoom0 = ChatRoom.builder()
                .sellerId(user.getId())
                .post(post)
                .sellerName(user.getName())
                .build();

        ChatRoom chatRoom1 = ChatRoom.builder()
                .userId(user.getId())
                .post(post)
                .build();

        chatRoomRepository.saveAll(Arrays.asList(chatRoom0, chatRoom1));

        // when
        List<ChatRoomResponseDto> responseDtoList = chatRoomService.retrieveChatRoomList(userPrincipal);

        // then
        assertThat(responseDtoList.get(0).getSellerId()).isEqualTo(user.getId());
        assertThat(responseDtoList.get(0).getPostId()).isEqualTo(post.getId());
        assertThat(responseDtoList.get(1).getUserId()).isEqualTo(user.getId());
    }
}