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
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager entityManager;

    private Setup setup = new Setup();

    private UserEntity userEntity;
    private UserPrincipal userPrincipal;
    private PostSaveRequestDto requestDto;
    private Book book;
    private Post post0;
    private Post post1;

//    @AfterEach
//    void clean() {
//        postRepository.deleteAll();
//        bookRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity();
        userRepository.save(userEntity);
        userPrincipal = UserPrincipal.createUserPrincipal(userEntity);
        requestDto = setup.createPostSaveRequestDto();

        book = requestDto.toBook();
        post0 = requestDto.toPost(userEntity);
//        post1 = setup.createPostSaveRequestDto().toPost(userEntity);

        book.addPost(post0);
        post0.addBook(book);
//        post1.addBook(book);

        postRepository.save(post0);
//        postRepository.saveAll(new ArrayList<>(Arrays.asList(post0, post1)));
    }

    @Test
    @DisplayName("POST 등록 테스트")
    void createPost() throws ParseException {
        //given

        //when
        PostResponseDto responseDto = postService.save(userPrincipal, requestDto);
        entityManager.clear();

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(requestDto.getPostTitle());
    }

    @Test
    @DisplayName("POST ID로 POST 상세 조회")
    void findPost() {
        //given

        //when
        entityManager.clear();
        PostResponseDto responseDto = postService.findById(userPrincipal, post0.getId());

        //then
        assertThat(responseDto.getPostTitle()).isEqualTo(requestDto.getPostTitle());
        assertThat(responseDto.getPostContent()).isEqualTo(requestDto.getPostContent());
        assertThat(responseDto.getBook().getBookTitle()).isEqualTo(requestDto.getBookTitle());
    }

    @Test
    @DisplayName("POST TITLE 로 포스트 조회")
    void findByPostTitle() {
        //given

        //when
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponseDto> page = postService.findByPostTitle(userPrincipal, post0.getTitle(), pageable);

        //then
        assertThat(page.getContent().get(0).getPostTitle()).contains(post0.getTitle());
    }


    @Test
    @DisplayName("전체 포스트 조회 테스트")
    void findAllPost() {
        //given

        //when
        entityManager.clear();
        Pageable pageable = PageRequest.of(0, 10);
        Page<PostResponseDto> page = postService.findAll(userPrincipal, pageable);

        //then
        assertThat(page.getContent().get(0).getPostTitle()).isEqualTo(post0.getTitle());
    }

    @Test
    @DisplayName("포스트 수정 테스트")
    void updatePost() {
        //given

        PostSaveRequestDto updateRequestDto = setup.createPostSaveRequestDto();

        //when
        entityManager.clear();
        PostResponseDto postResponseDto = postService.updatePost(post0.getId(), userPrincipal, updateRequestDto);

        //then
        assertThat(postResponseDto.getPostTitle()).isEqualTo(updateRequestDto.getPostTitle());
    }

    @Test
    @DisplayName("포스트 삭제 테스트")
    void deletePost() {
        //given

        //when
        entityManager.clear();
        postService.delete(post0.getId(), userPrincipal);

        //then
        System.out.println("삭제 완료");
    }
}
