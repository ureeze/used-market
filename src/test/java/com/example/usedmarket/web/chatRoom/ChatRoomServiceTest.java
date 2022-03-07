package com.example.usedmarket.web.chatRoom;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.chatRoom.ChatRoom;
import com.example.usedmarket.web.domain.chatRoom.ChatRoomRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.ChatRoomListResponseDto;
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

//    @Autowired
//    private ChatRoomService chatRoomService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//
//    private final Setup setup = new Setup();
//
//    @Test
//    @DisplayName("채팅방 생성")
//    void createChat() {
//        // given
//        // UserEntity, UserPrincipal
//        UserEntity seller = setup.createUserEntity(0);
//        UserEntity buyer = setup.createUserEntity(1);
//        userRepository.saveAll(Arrays.asList(buyer, seller));
//        UserPrincipal buyerPrincipal = UserPrincipal.createUserPrincipal(buyer);
//
//        // Post
//        Post post = setup.createPost(seller, 0);
//        postRepository.save(post);
//
////        // ChatRoomCreateRequestDto
////        ChatRoomCreateRequestDto requestDto = ChatRoomCreateRequestDto.builder()
////                .postId(post.getId())
////                .build();
//
//        //when
//        ChatRoomResponseDto responseDto = chatRoomService.createChat(buyerPrincipal, post.getId());
//
//        //then
//        assertThat(responseDto.getPostTitle()).isEqualTo(post.getTitle());
//        assertThat(responseDto.getSellerId()).isEqualTo(seller.getId());
//    }
//
//    @Test
//    @DisplayName("sellerId 에 대한 채팅방리스트 조회")
//    void retrieveChatRoomListOfSeller() {
//        // given
//        // UserEntity, UserPrincipal
//        UserEntity seller = setup.createUserEntity(0);
//        userRepository.save(seller);
//
//        // Post
//        Post post = setup.createPost(seller, 0);
//        postRepository.save(post);
//
//        // ChatRoom
//        ChatRoom chatRoom = ChatRoom.builder()
//                .sellerId(seller.getId())
//                .post(post)
//                .sellerName(seller.getName())
//                .build();
//
//        chatRoomRepository.save(chatRoom);
//
//        // when
//        List<ChatRoomResponseDto> responseDtoList = chatRoomService.retrieveChatRoomListOfSeller(post.getUserEntity().getId());
//
//        // then
//        assertThat(responseDtoList.get(0).getSellerId()).isEqualTo(seller.getId());
//        assertThat(responseDtoList.get(0).getPostId()).isEqualTo(post.getId());
//    }
//
//    @Test
//    @DisplayName("구매하는 경우와 판매하려는 경우에 대한 ChatRoom 리스트 조회")
//    void retrieveChatRoomList() {
//        // given
//        // UserEntity, UserPrincipal
//        UserEntity user = setup.createUserEntity(0);
//        userRepository.save(user);
//        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(user);
//
//        // Post
//        Post post = setup.createPost(user, 0);
//        postRepository.save(post);
//
//        // ChatRoom
//        ChatRoom chatRoom0 = ChatRoom.builder()
//                .sellerId(user.getId())
//                .post(post)
//                .sellerName(user.getName())
//                .build();
//
//        ChatRoom chatRoom1 = ChatRoom.builder()
//                .userId(user.getId())
//                .post(post)
//                .build();
//
//        chatRoomRepository.saveAll(Arrays.asList(chatRoom0, chatRoom1));
//
//        // when
//        ChatRoomListResponseDto chatRoomListResponseDto = chatRoomService.retrieveChatRoomList(userPrincipal);
//
//        // then
//        assertThat(chatRoomListResponseDto.getChatRoomListBySeller().get(0).getChatRoomId()).isEqualTo(chatRoom0.getId());
//        assertThat(chatRoomListResponseDto.getChatRoomListByBuyer().get(0).getChatRoomId()).isEqualTo(chatRoom1.getId());
//    }
}