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
    //POST ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //POST 제목
    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    //POST 내용
    @Column(name = "CONTENT", length = 200, nullable = false)
    private String content;

    //POST 상태
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    //POST 삭제 여부
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    //POST 와 관련된 MEMBER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    //POST 와 관련된 BOOK
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
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

    // POST 책 추가
    public Post addBook(Book book) {
        getBookList().add(book);
        return this;
    }

    // 포스트 수정
    public void update(PostSaveRequestDto requestDto) {
        // POST 제목 수정
        this.title = requestDto.getTitle();

        // POST 내용 수정
        this.content = requestDto.getContent();
    }

    //POST 삭제 여부 확인
    public boolean isDeletable(Member member) {
        if (this.member != member) {
            throw new IllegalArgumentException("허용 되지 않은 사용자입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 POST 입니다.");
        }

        return true;
    }

    //POST 삭제
    public void deleted() {
        this.bookList.get(0).deleted();
        this.deleted = true;
    }
}
