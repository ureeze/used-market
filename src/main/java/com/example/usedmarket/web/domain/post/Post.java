package com.example.usedmarket.web.domain.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import lombok.*;
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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 30)
    private String title;

    @Column(name = "CONTENT", length = 200, nullable = false)
    private String content;

    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private List<Book> bookList = new ArrayList<>();

    @Builder
    public Post(String title, String content, Status status, Member member) {
        this.title = title;
        this.content = content;
        this.status = status;
        this.member = member;
    }

    public static Post toPost(Member member, PostSaveRequestDto postRequest) {
        return Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .status(Status.SELL)
                .member(member)
                .build();
    }

    public void update(PostSaveRequestDto postSaveRequestDto) {
        this.title = postSaveRequestDto.getTitle();
        this.content = postSaveRequestDto.getContent();
    }
}
