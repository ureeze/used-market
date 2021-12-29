package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.book.BookStatus;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.user.Role;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postsRepository;

    @Autowired
    BookRepository booksRepository;

    @Autowired
    TestEntityManager testEntityManager;

    /*
    FormLogin 용 UserEntity 생성
     */
    UserEntity createUserEntity() {
        int num = (int) (Math.random() * 10000) + 1;
        String name = "PBJ" + num;
        String email = name + "@google.com";
        String password = num + "";

        UserEntity userEntity = UserEntity.builder()
                .name(name).password(password)
                .email(email).picture(null).registrationId(null)
                .role(Role.USER)
                .build();
        return userEntity;
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
        return booksRepository.save(book);
    }

    Post createPost(UserEntity user, Book book) {
        int num = (int) (Math.random() * 10000) + 1;
        Post post = Post.builder()
                .title("PostTitle" + num)
                .content("contentInPost" + num)
                .status(PostStatus.SELL)
                .userEntity(user)
                .build();
        post.addBook(book);
        return post;
    }


    @Test
    @DisplayName("repository - 포스트 저장")
    void CreatePostAndBook() {
        //given
        UserEntity userEntity = createUserEntity();
        Book book = createBook();
        Post post = createPost(userEntity, book);

        //when
        testEntityManager.persist(post);

        //then
        assertThat(postsRepository.findById(post.getId()).get()).isEqualTo(post);
    }
}