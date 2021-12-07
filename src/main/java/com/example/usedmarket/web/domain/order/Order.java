package com.example.usedmarket.web.domain.order;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RECIPIENT", nullable = false, length = 10)
    private String recipient;

    @Column(name = "ADDRESS", nullable = false, length = 200)
    private String address;

    @Column(name = "PHONE", nullable = false, length = 30)
    private String phone;

    @Column(name = "DELIVERY_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderedBook> orderedBookList = new ArrayList<>();

    @Builder
    public Order(String recipient, String address, String phone, DeliveryStatus deliveryStatus, Member member, Post post, boolean deleted) {
        this.recipient = recipient;
        this.address = address;
        this.phone = phone;
        this.deliveryStatus = deliveryStatus;
        this.member = member;
        this.post = post;
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Order) {
            Order order = (Order) obj;
            if (id.equals(order.id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDeletable(Member member) {
        if (this.member != member) {
            throw new IllegalArgumentException("허용 되지 않은 사용자입니다.");
        }

        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 ORDER 입니다.");
        }

        return true;
    }

    public void deleted() {
        this.deleted = true;
    }


}
