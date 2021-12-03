package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.config.auth.dto.SessionMember;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.PostSaveResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BookRepository bookRepository;

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
    }

    public SessionMember retrieveSessionMember() {
        Member member = Member.builder()
                .name("pbj")
                .email("pbj@naver.com")
                .picture("pic")
                .role(Role.USER)
                .build();
        SessionMember sessionMember = new SessionMember(memberRepository.save(member));
        return sessionMember;

    }

    public PostSaveRequestDto retrieveRequestDto() {
        return PostSaveRequestDto.builder()
                .title("스프링부트 책 팝니다.")
                .content("스프링부트2.6")
                .bookName("스프링부트로 앱 만들기")
                .stock(1)
                .unitPrice(10000)
                .category("it")
                .build();
    }

    @Test
    @DisplayName("service - 포스트 생성 테스트")
    public void createPost() {
        //given
        SessionMember sessionMember = retrieveSessionMember();
        PostSaveRequestDto requestDto = retrieveRequestDto();

        //when
        PostSaveResponseDto responseDto = postService.save(sessionMember, requestDto);

        //then
        assertEquals(requestDto.getContent(), responseDto.getContent());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getBookName(), responseDto.getBookList().get(0).getBookName());
        assertEquals(bookRepository.findAll().get(0).getId(), responseDto.getBookList().get(0).getId());
    }

    @Test
    @DisplayName("service - 포스트 조회 테스트")
    public void findPost() {
        //given
        SessionMember sessionMember = retrieveSessionMember();
        Post post = Post.toPost(Member.toMember(sessionMember), retrieveRequestDto());
        Book book = Book.toBook(retrieveRequestDto());
        post.getBookList().add(book);

        //when
        postRepository.save(post);
        PostSaveResponseDto responseDto = postService.findById(post.getId());

        //then
        assertEquals(post.getId(), responseDto.getPostId());
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getMember().getId(), responseDto.getMemberId());
        assertEquals(post.getBookList().get(0).getBookName(), responseDto.getBookList().get(0).getBookName());
        assertEquals(bookRepository.findAll().get(0).getId(), responseDto.getBookList().get(0).getId());
    }

    @Test
    @DisplayName("service - 전체 포스트 조회 테스트")
    public void findAllPost() {
        //given
        SessionMember sessionMember = retrieveSessionMember();
        Post post0 = Post.toPost(Member.toMember(sessionMember), retrieveRequestDto());
        Post post1 = Post.toPost(Member.toMember(sessionMember), retrieveRequestDto());
        Book book = Book.toBook(retrieveRequestDto());
        post0.getBookList().add(book);
        post1.getBookList().add(book);

        //when
        postRepository.save(post0);
        postRepository.save(post1);
        List<PostSaveResponseDto> postList = postService.findAll();

        //then
        assertEquals(post0.getId(), postList.get(0).getPostId());
        assertEquals(post1.getId(), postList.get(1).getPostId());
    }

    @Test
    @DisplayName("service - 포스트 수정 테스트")
    public void updatePost() {
        //given
        SessionMember sessionMember = retrieveSessionMember();
        Post post = Post.toPost(Member.toMember(sessionMember), retrieveRequestDto());
        Book book = Book.toBook(retrieveRequestDto());
        post.getBookList().add(book);
        postRepository.save(post);

        PostSaveRequestDto newRequestDto = PostSaveRequestDto.builder()
                .title("자바 책 팝니다.")
                .content("스프링부트2.6")
                .bookName("자바 만들기")
                .stock(1)
                .unitPrice(10000)
                .category("it")
                .build();

        //when
        postService.update(post.getId(), newRequestDto);

        //then
        postRepository.findById(post.getId());
        assertEquals(newRequestDto.getTitle(), post.getTitle());
        assertEquals(newRequestDto.getBookName(), post.getBookList().get(0).getBookName());
    }

    @Test
    @DisplayName("service - 포스트 삭제 테스트")
    public void deletePost() {
        //given
        Post post = Post.toPost(Member.toMember(retrieveSessionMember()), retrieveRequestDto());
        Book book = Book.toBook(retrieveRequestDto());
        post.getBookList().add(book);

        //when
        postRepository.save(post);
        postService.delete(post.getId());

        //then
        System.out.println("service - 포스트 삭제 성공");
    }
}