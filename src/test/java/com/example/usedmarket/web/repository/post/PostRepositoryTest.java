package com.example.usedmarket.web.repository.post;

import com.example.usedmarket.web.repository.member.Member;
import com.example.usedmarket.web.repository.member.MemberRepository;
import com.example.usedmarket.web.repository.member.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;


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
    @DisplayName("도서 제외한 글 레포지토리 등록")
    public void postCreateExceptBook() {
        //given
        Member member = createMember();
        Post post = Post.builder()
                .title("스프링부트")
                .content("스프링부트2.0은 스프링프레임워크의 ....")
                .status(Status.SELL)
                .member(member)
                .build();

        //when
        Post savedPost = postRepository.save(post);

        //then
        assertEquals(post.getTitle(),savedPost.getTitle());
        assertEquals(post.getContent(), savedPost.getContent());
        assertEquals(post.getMember().getId(), savedPost.getMember().getId());
    }
}