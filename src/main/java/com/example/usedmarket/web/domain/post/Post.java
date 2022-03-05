package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseTimeEntity implements Serializable {
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

    //POST 상태 (SELL, SOLD_OUT, DELETED)
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    //POST 삭제 여부
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    //POST 와 관련된 USER
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    //POST 와 관련된 BOOK
    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Book> bookList = new ArrayList<>();

    @Builder
    public Post(String title, String content, PostStatus status, UserEntity userEntity, boolean deleted) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.userEntity = userEntity;
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
            if (id.equals(post.id) && title.equals(post.title)) {
                return true;
            }
        }
        return false;
    }


    // POST 책 추가
    public void addBook(Book book) {
        this.bookList.add(book);
    }

    // 포스트 수정
    public void update(PostSaveRequestDto requestDto) {
        // POST 제목 수정
        this.title = requestDto.getPostTitle();

        // POST 내용 수정
        this.content = requestDto.getPostContent();
    }

    // 포스트에 등록된 책 수정
    public void updateBook(PostSaveRequestDto requestDto) {
        this.getBookList().get(0).update(requestDto);
    }

    //POST 삭제 여부 확인
    public boolean isDeletable(Long userId) {
        if (this.userEntity.getId() != userId) {
            throw new IllegalArgumentException("허용 되지 않은 사용자입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 POST 입니다.");
        }

        return true;
    }

    //POST 삭제
    public void deleted() {
        //BOOK isDeleted 처리
        this.bookList.get(0).deleted();
        //POST isDeleted 처리
        this.deleted = true;
        //POST 판매상태 변경(DELETED)
        this.status = PostStatus.DELETED;
    }

    //POST 가 SOLD_OUT 상태인지 체크
    public boolean isSoldOut() {
        if (this.status == PostStatus.SOLD_OUT) {
            return true;
        } else {
            return false;
        }
    }

    //POST 의 상태를 SELL 로 전환
    public void changeToStatusIsSell() {
        this.status = PostStatus.SELL;
    }

    //POST 판매상태 변경
    public void changeToSoldOut() {
        this.status = PostStatus.SOLD_OUT;
    }
}