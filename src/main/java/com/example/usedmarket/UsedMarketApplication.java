package com.example.usedmarket;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.PostStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.IntStream;

@EnableJpaAuditing
@SpringBootApplication
public class UsedMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsedMarketApplication.class, args);
    }


    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(MemberRepository memberRepository, PostRepository postRepository, BookRepository bookRepository) {

        return (args) -> {
            System.out.println("dataLoader 시작");
            Member member = memberRepository.save(Member.builder()
                    .name("default 이름")
                    .role(Role.USER)
                    .email("default@gmail.com")
                    .picture("picDefault")
                    .build());


            IntStream.rangeClosed(1, 10).forEach(index -> {

                        Post post = Post.builder()
                                .title("게시글" + index)
                                .content("콘텐츠" + index)
                                .status(PostStatus.SELL)
                                .deleted(false)
                                .member(member)
                                .build();
                        Book book = Book.builder()
                                .bookName("name" + index)
                                .imgUrl("img" + index)
                                .unitPrice(10000)
                                .category("cate")
                                .deleted(false)
                                .bookStatus(BookStatus.S)
                                .stock(1)
                                .build();
                        book.addPost(post);
                        post.addBook(book);
                        postRepository.save(post);
                    }

            );
            postRepository.flush();
        };
    }
}
