package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
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
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        bookRepository.deleteAll();
        memberRepository.deleteAll();
    }

    Member createMember() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "pbj" + num;
        String email = name + "@google.com";
        String picture = "pbjPicture" + num;
        Member member = Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .picture(picture)
                .build();
        return memberRepository.save(member);
    }

    Book createBook() {
        int num = (int) (Math.random() * 10000) + 1;
        Book book = Book.builder()
                .bookName("bookTitle")
                .category("it")
                .imgUrl("url")
                .unitPrice(10000)
                .bookStatus(BookStatus.S)
                .stock(1)
                .build();
        return bookRepository.save(book);
    }

    Post createPost(Member member, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        Post post = Post.builder()
                .title("PostTitle" + num)
                .content("contentInPost" + num)
                .status(PostStatus.SELL)
                .member(member)
                .build();
        return post.addBook(book);

    }

    @Test
    @DisplayName("repository - 포스트 저장")
    void CreatePostAndBook() {
        //given
        Member member = createMember();
        Book book = createBook();
        Post post = createPost(member, book);

        //when
        postRepository.saveAndFlush(post);

        //then
        Post savedPost = postRepository.findById(post.getId()).get();
        assertEquals(post.getTitle(), savedPost.getTitle());
        assertEquals(post.getContent(), savedPost.getContent());
        assertEquals(post.getStatus(), savedPost.getStatus());
        assertEquals(book.getBookName(), savedPost.getBookList().get(0).getBookName());
        assertEquals(book.getCategory(), savedPost.getBookList().get(0).getCategory());
        assertEquals(post.getMember().getEmail(), savedPost.getMember().getEmail());
        assertEquals(false, savedPost.isDeleted());

    }
}