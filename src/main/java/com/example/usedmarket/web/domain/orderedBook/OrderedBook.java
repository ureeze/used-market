package com.example.usedmarket.web.domain.orderedBook;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COUNT", nullable = false)
    private int count;

    @Column(name = "ORDER_PRICE", nullable = false)
    private int orderPrice;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    public boolean isDeletable(Order order) {
        if (this.order != order) {
            throw new IllegalArgumentException("허용 되지 않은 주문입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 책 입니다.");
        }

        return true;
    }

    public void deleted() {
        this.deleted = true;
    }

}