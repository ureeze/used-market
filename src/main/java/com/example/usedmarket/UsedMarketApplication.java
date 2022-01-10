package com.example.usedmarket;

import com.example.usedmarket.web.config.AppProperties;
import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.post.PostStatus;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.IntStream;

//@EnableCaching
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class UsedMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsedMarketApplication.class, args);
    }


    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(UserRepository userRepository, PostRepository postRepository, BookRepository bookRepository) {

        return (args) -> {
            System.out.println("dataLoader 시작");
            UserEntity userEntity = userRepository.save(UserEntity.builder()
                    .name("사용자_이름")
                    .role(Role.USER)
                    .email("default@gmail.com")
                    .picture("picDefault")
                    .build());


            IntStream.rangeClosed(1, 10).forEach(index -> {

                        Post post = Post.builder()
                                .title("포스트_제목_" + index)
                                .content("포스트_CONTENT_" + index)
                                .status(PostStatus.SELL)
                                .deleted(false)
                                .userEntity(userEntity)
                                .build();
                        Book book = Book.builder()
                                .title("BOOK_제목_" + index)
                                .imgUrl("http://bookthumb.phinf.naver.net/cover/108/346/10834650.jpg?type=m1&udate=20160902")
                                .unitPrice(10000)
                                .category("정보/컴퓨터")
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
