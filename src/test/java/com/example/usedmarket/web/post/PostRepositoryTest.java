package com.example.usedmarket.web.post;

import com.example.usedmarket.config.TestConfig;
import com.example.usedmarket.web.Setup;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(TestConfig.class)
public class PostRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postsRepository;

    @Autowired
    BookRepository booksRepository;

    @Autowired
    TestEntityManager testEntityManager;

    private Setup setup = new Setup();
    private UserEntity userEntity;
    private Book book;
    private Post post;

    @BeforeEach
    void setup() {
        userEntity = setup.createUserEntity();
        book = setup.createBook();
        post = setup.createPost(userEntity);
        book.addPost(post);
        post.addBook(book);
    }

    @Test
    @DisplayName("포스트 저장")
    void CreatePostAndBook() {
        //given

        //when
        testEntityManager.persist(post);
        testEntityManager.clear();

        //then
        assertThat(postsRepository.findById(post.getId()).get()).isEqualTo(post);
    }
}