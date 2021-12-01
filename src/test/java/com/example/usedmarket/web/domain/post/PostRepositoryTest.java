package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
    }

    public Member createMember() {
        String name = "pbj";
        String email = "pbj@google.com";
        String picture = "pbjPicture";
        Member member = Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .picture(picture)
                .build();
        return memberRepository.save(member);
    }


    @Test
    @DisplayName("repository - 포스트 레포지토리 등록")
    public void CreatePostAndBook() {
        //given
        Member member = createMember();
        Post post = Post.builder()
                .title("스프링부트")
                .content("스프링부트2.0은 스프링프레임워크의 ....")
                .status(Status.SELL)
                .member(member)
                .build();
        Book book = Book.builder()
                .bookName("책 이름")
                .stock(1)
                .unitPrice(10000)
                .category("it")
                .imgUrl("img")
                .build();

        post.getBookList().add(book);

        //when
        postRepository.saveAndFlush(post);

        //then
        Post savedPost = postRepository.findById(post.getId()).get();
        assertEquals("책 이름", savedPost.getBookList().get(0).getBookName());
    }
}