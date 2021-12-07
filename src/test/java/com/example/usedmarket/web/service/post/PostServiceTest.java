package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.security.dto.SessionMember;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void clean() {
        postRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    SessionMember createSessionMember() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        Member member = Member.builder()
                .name(name)
                .email(name + "@google.com")
                .picture("pic" + num)
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        SessionMember sessionMember = new SessionMember(memberRepository.save(member));
        return sessionMember;
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
        SessionMember sessionMember = createSessionMember();
        PostSaveRequestDto requestDto = createRequestDto();

        //when
        PostResponseDto responseDto = postService.save(sessionMember, requestDto);

        //then
        assertEquals(requestDto.getContent(), responseDto.getContent());
        assertEquals(requestDto.getTitle(), responseDto.getTitle());
        assertEquals(requestDto.getBookName(), responseDto.getBookList().get(0).getBookName());
    }

    @Test
    @DisplayName("service - 포스트 개별 조회 테스트")
    void findPost() {
        //given
        SessionMember sessionMember = createSessionMember();

        PostSaveRequestDto requestDto = createRequestDto();

        Post post = requestDto.toPost(sessionMember);

        Book book = requestDto.toBook();
        post.addBook(book);

        postRepository.save(post);

        //when
        PostResponseDto responseDto = postService.findById(post.getId());

        //then
        assertEquals(post.getId(), responseDto.getPostId());
        assertEquals(post.getTitle(), responseDto.getTitle());
        assertEquals(post.getMember().getId(), responseDto.getMemberId());
        assertEquals(post.getBookList().get(0).getBookName(), responseDto.getBookList().get(0).getBookName());
        assertEquals(post.getBookList().get(0).getId(), bookRepository.findAll().get(0).getId());
    }

    @Test
    @DisplayName("service - 전체 포스트 조회 테스트")
    void findAllPost() {
        //given
        SessionMember sessionMember = createSessionMember();

        PostSaveRequestDto requestDto = createRequestDto();

        Post post0 = requestDto.toPost(sessionMember);
        Post post1 = createRequestDto().toPost(sessionMember);

        Book book = requestDto.toBook();
        post0.addBook(book);
        post1.addBook(book);

        postRepository.save(post0);
        postRepository.save(post1);

        //when
        List<PostResponseDto> findAll = postService.findAll();

        //then
        assertEquals(post0.getId(), findAll.get(0).getPostId());
        assertEquals(post1.getId(), findAll.get(1).getPostId());
        assertEquals(post1.getBookList().get(0).getBookName(), findAll.get(1).getBookList().get(0).getBookName());
    }

    @Test
    @DisplayName("service - 포스트 수정 테스트")
    void updatePost() {
        //given
        SessionMember sessionMember = createSessionMember();

        PostSaveRequestDto requestDto = createRequestDto();

        Post post = requestDto.toPost(sessionMember);

        Book book = requestDto.toBook();
        post.addBook(book);

        postRepository.save(post);

        PostSaveRequestDto newRequestDto = createRequestDto();

        //when
        PostResponseDto postResponseDto = postService.update(post.getId(), newRequestDto);

        //then
        assertEquals(newRequestDto.getTitle(), postResponseDto.getTitle());
        assertEquals(newRequestDto.getBookName(), postResponseDto.getBookList().get(0).getBookName());
    }

    @Test
    @DisplayName("service - 포스트 삭제 테스트")
    void deletePost() {
        //given
        SessionMember sessionMember = createSessionMember();

        PostSaveRequestDto requestDto = createRequestDto();

        Post post = requestDto.toPost(sessionMember);

        Book book = requestDto.toBook();
        post.addBook(book);

        Post savedPost = postRepository.save(post);

        //when
        postService.delete(savedPost.getId());

        //then
        System.out.println("service - 포스트 삭제 성공");
    }
}