package com.example.usedmarket.web.domain.book;


import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "BOOK")
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseTimeEntity {

    //Book ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //책 제목
    @Column(name = "BOOK_NAME", nullable = false, length = 50)
    private String bookName;

    //책 재고
    @Column(name = "STOCK", nullable = false)
    private int stock;

    //책 권당 가격
    @Column(name = "UNIT_PRICE", nullable = false)
    private int unitPrice;

    //책 카테고리
    @Column(name = "CATEGORY", nullable = false, length = 20)
    private String category;

    //책 삭제여부
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    //책 상태
    @Column
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    //책 이미지 주소
    @Column(name = "IMG_URL", length = 500)
    private String imgUrl;

    //책과 연결되 있는 POST
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    // 책 정보 수정
    public void update(PostSaveRequestDto requestDto) {
        this.bookName = requestDto.getBookName();
        this.stock = requestDto.getStock();
        this.unitPrice = requestDto.getUnitPrice();
        this.category = requestDto.getCategory();
        this.imgUrl = requestDto.getImgUrl();
    }

    public void addPost(Post post) {
        this.post = post;
    }

    // 주문취소로 인한 책 재고 증가
    public void stockUp(int count) {
        this.stock += count;
    }

    // 책 삭제
    public void deleted() {
        this.deleted = true;
    }
}
