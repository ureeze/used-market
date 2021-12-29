package com.example.usedmarket.web.domain.orderedBook;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.order.Order;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "ORDERED_BOOK")
@EntityListeners(AuditingEntityListener.class)
public class OrderedBook extends BaseTimeEntity {
    //ORDERED BOOK ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //주문한 책 수량
    @Column(name = "COUNT", nullable = false)
    private int count;

    //주문 가격
    @Column(name = "ORDER_PRICE", nullable = false)
    private int orderPrice;

    //삭제 여부
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    //ORDERED BOOK 와 관련된 ORDER
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    //ORDERED BOOK 와 관련된 BOOK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    //ORDERED BOOK 삭제 가능 확인
    public boolean isDeletable(Order order) {
        if (this.order != order) {
            throw new IllegalArgumentException("허용 되지 않은 주문입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 책 입니다.");
        }

        return true;
    }

    //ORDERED BOOK 삭제
    public void deleted() {
        this.deleted = true;
    }

    //ORDER 추가
    public void addOrder(Order order) {
        this.order = order;
    }
}
