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
@Table(name = "BOOK")
@EntityListeners(AuditingEntityListener.class)
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "BOOK_NAME", nullable = false, length = 50)
    private String bookName;

    @Column(name = "STOCK", nullable = false)
    private int stock;

    @Column(name = "UNIT_PRICE", nullable = false)
    private int unitPrice;

    @Column(name = "CATEGORY", nullable = false, length = 20)
    private String category;

    @Column(name = "IMG_URL", length = 500)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Book(String bookName, int stock, int unitPrice, String category, String imgUrl) {
        this.bookName = bookName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.category = category;
        this.imgUrl = imgUrl;
    }

    public static Book toBook(PostSaveRequestDto requestDto) {
        return Book.builder()
                .bookName(requestDto.getBookName())
                .stock(requestDto.getStock())
                .unitPrice(requestDto.getUnitPrice())
                .category(requestDto.getCategory())
                .imgUrl(requestDto.getImgUrl())
                .build();
    }

    public void update(PostSaveRequestDto requestDto) {
        this.bookName = requestDto.getBookName();
        this.stock = requestDto.getStock();
        this.unitPrice = requestDto.getUnitPrice();
        this.category = requestDto.getCategory();
        this.imgUrl = requestDto.getImgUrl();
    }
}
