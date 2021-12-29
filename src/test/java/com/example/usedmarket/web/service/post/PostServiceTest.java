package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.dto.PostSaveResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
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
public class PostServiceTest {
    @Autowired
    PostServiceImpl postService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    UserPrincipal createUserPrincipal() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        UserEntity user = UserEntity.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return UserPrincipal.createUserPrincipal(user);
    }

    PostSaveRequestDto createRequestDto() {
        int num = (int) (Math.random() * 10000) + 1;

        return PostSaveRequestDto.builder()
                .title("스프링부트 책 팝니다." + num)
                .content("스프링부트는 스프링 프레임워크의 복잡한 환경설정을 간편하게 해놓은 ..." + num)
                .bookName("스프링부트로 앱 만들기" + num)
                .stock(1)
                .unitPrice(num)
                .category("it" + num)
                .bookStatus("S")
                .build();
    }

    @Test
    @DisplayName("service - 포스트 생성 테스트")
    void createPost() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        PostSaveRequestDto requestDto = createRequestDto();

        //when
        PostSaveResponseDto responseDto = postService.save(userPrincipal, requestDto);

        //then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
        assertThat(responseDto.getBooksList().get(0).getBookName()).isEqualTo(requestDto.getBookName());
    }

    @Test
    @DisplayName("service - 포스트 개별 조회 테스트")
    void findPost() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        UserEntity user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
        PostSaveRequestDto requestDto = createRequestDto();

        Book book = requestDto.toBook();
        Post post = requestDto.toPost(user, book);

        postRepository.save(post);

        //when
        PostSaveResponseDto responseDto = postService.findById(post.getId());

        //then
        assertThat(responseDto.getContent()).isEqualTo(requestDto.getContent());
    }

    @Test
    @DisplayName("service - 전체 포스트 조회 테스트")
    void findAllPost() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        UserEntity user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        PostSaveRequestDto requestDto = createRequestDto();

        Book book = requestDto.toBook();
        Post post0 = requestDto.toPost(user, book);
        Post post1 = createRequestDto().toPost(user, book);

        postRepository.save(post0);
        postRepository.save(post1);

        //when
        List<PostSaveResponseDto> findAll = postService.findAll();

        //then
        assertThat(findAll.get(0).getContent()).isEqualTo(post0.getContent());
        assertThat(findAll.get(1).getContent()).isEqualTo(post1.getContent());
    }

    @Test
    @DisplayName("service - 포스트 수정 테스트")
    void updatePost() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        UserEntity user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        PostSaveRequestDto requestDto = createRequestDto();


        Book book = requestDto.toBook();
        Post post = requestDto.toPost(user, book);


        postRepository.save(post);

        PostSaveRequestDto newRequestDto = createRequestDto();

        //when
        PostSaveResponseDto postResponseDto = postService.update(post.getId(), newRequestDto);

        //then
        assertThat(postResponseDto.getContent()).isEqualTo(newRequestDto.getContent());
    }

    @Test
    @DisplayName("service - 포스트 삭제 테스트")
    void deletePost() {
        //given
        UserPrincipal userPrincipal = createUserPrincipal();
        UserEntity user = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        PostSaveRequestDto requestDto = createRequestDto();

        Book book = requestDto.toBook();
        Post post = requestDto.toPost(user, book);
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        System.out.println("삭제 완료");
    }
}
