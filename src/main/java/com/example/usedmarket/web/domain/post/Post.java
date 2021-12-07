package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @Column(name = "CONTENT", length = 200, nullable = false)
    private String content;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Book> bookList = new ArrayList<>();

    @Builder
    public Post(String title, String content, PostStatus status, Member member, boolean deleted) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.member = member;
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            Post post = (Post) obj;
            if (id.equals(post.id)) {
                return true;
            }
        }
        return false;
    }

    public Post addBook(Book book) {
        getBookList().add(book);
        return this;
    }

    public void update(PostSaveRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.bookList.get(0).update(requestDto);
    }

    public boolean isDeletable(Member member) {
        if (this.member != member) {
            throw new IllegalArgumentException("허용 되지 않은 사용자입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 POST 입니다.");
        }

        return true;
    }

    public void deleted() {
        this.bookList.get(0).deleted();
        this.deleted = true;
    }
}
