package com.example.usedmarket.web.post;

import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostDetailsResponseDto;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.post.PostServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Setup setup = new Setup();

//    @AfterEach
//    void clean() {
//        postRepository.deleteAll();
//        bookRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    @DisplayName("포스트 생성 테스트")
    void createPost() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        //when
        PostResponseDto responseDto = postService.save(userPrincipal, requestDto);

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(requestDto.getPostTitle());
    }

    @Test
    @DisplayName("포스트 개별 조회 테스트")
    void findPost() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);

        //when
        PostDetailsResponseDto responseDto = postService.findById(post.getId());

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(requestDto.getPostTitle());
        assertThat(responseDto.getPostContent()).isEqualTo(requestDto.getPostContent());
    }

    @Test
    @DisplayName("POST TITLE 로 포스트 조회")
    void findByPostTitle() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        Book book = requestDto.toBook();
        Post post0 = requestDto.toPost(userEntity);
        Post post1 = setup.createPostSaveRequestDto().toPost(userEntity);

        book.addPost(post0);
        post0.addBook(book);
        post1.addBook(book);

        postRepository.saveAll(new ArrayList<>(Arrays.asList(post0, post1)));

        //when
        List<PostResponseDto> responseDto = postService.findByPostTitle("스프링부트");

        //then
        assertThat(responseDto.get(0).getPostTitle()).contains("스프링부트");
        assertThat(responseDto.get(1).getPostTitle()).contains("스프링부트");
    }


    @Test
    @DisplayName("전체 포스트 조회 테스트")
    void findAllPost() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        Book book = requestDto.toBook();
        Post post0 = requestDto.toPost(userEntity);
        Post post1 = setup.createPostSaveRequestDto().toPost(userEntity);

        book.addPost(post0);
        post0.addBook(book);
        post1.addBook(book);

        postRepository.saveAll(new ArrayList<>(Arrays.asList(post0, post1)));

        //when
        List<PostResponseDto> findAll = postService.findAll();

        //then
        assertThat(findAll.get(0).getPostTitle()).isEqualTo(post0.getTitle());
        assertThat(findAll.get(1).getPostTitle()).isEqualTo(post1.getTitle());
    }

    @Test
    @DisplayName("포스트 수정 테스트")
    void updatePost() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);

        postRepository.save(post);

        PostSaveRequestDto updateRequestDto = setup.createPostSaveRequestDto();

        //when
        PostResponseDto postResponseDto = postService.updatePost(post.getId(), userPrincipal, updateRequestDto);

        //then
        assertThat(postResponseDto.getPostTitle()).isEqualTo(updateRequestDto.getPostTitle());
    }

    @Test
    @DisplayName("포스트 삭제 테스트")
    void deletePost() {
        //given
        UserEntity userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        UserPrincipal userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        PostSaveRequestDto requestDto = setup.createPostSaveRequestDto();

        Book book = requestDto.toBook();
        Post post = requestDto.toPost(userEntity);
        book.addPost(post);
        post.addBook(book);
        postRepository.save(post);

        //when
        postService.delete(post.getId(), userPrincipal);

        //then
        System.out.println("삭제 완료");
    }
}
