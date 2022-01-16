package com.example.usedmarket.web.domain.order;

import com.example.usedmarket.web.domain.BaseTimeEntity;
import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.exception.OrderCancellationNotAllowed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDERS")
@EntityListeners(AuditingEntityListener.class)
public class Order extends BaseTimeEntity {
    // ORDER ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 받는 사람명
    @Column(name = "RECIPIENT", nullable = false, length = 10)
    private String recipient;

    // 배송 주소
    @Column(name = "ADDRESS", nullable = false, length = 200)
    private String address;

    // 연락처
    @Column(name = "PHONE", nullable = false, length = 30)
    private String phone;

    // 배송 상태 (CANCEL_COMPLETED, PAYMENT_COMPLETED, BEING_DELIVERED, DELIVERY_COMPLETED)
    @Column(name = "DELIVERY_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    // 삭제 여부
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;

    // 주문과 연관된 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    // 주문과 연관된 POST
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    // 주문과 연관된 ORDERED BOOK
    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderedBook> orderedBookList = new ArrayList<>();

    @Builder
    public Order(String recipient, String address, String phone, DeliveryStatus deliveryStatus, UserEntity userEntity, Post post, boolean deleted) {
        this.recipient = recipient;
        this.address = address;
        this.phone = phone;
        this.deliveryStatus = deliveryStatus;
        this.user = userEntity;
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
            if (id == order.id) {
                return true;
            }
        }
        return false;
    }

    //주문 삭제 가능 확인
    public boolean isDeletable(UserEntity userEntity) {
        if (this.user != userEntity) {
            throw new IllegalArgumentException("허용 되지 않은 사용자입니다.");
        }
        if (this.deleted) {
            throw new IllegalArgumentException("이미 삭제된 ORDER 입니다.");
        }
        return true;
    }

    // 주문 삭제
    public void deleted() {
        this.deleted = true;
    }

    // 주문 취소 로직
    public void cancel(UserEntity userEntity) {
        // 주문 취소
        if (isDeletable(userEntity)) {
            deleted();
            // 배송상태를 주문취소로 변경
            this.changeDeliveryStatusToCancel();
        }else {
            //주문취소 불가
            throw new OrderCancellationNotAllowed("주문 취소 불가");
        }
    }

    //배송상태를 주문취소로 변경
    public void changeDeliveryStatusToCancel() {
        switch (this.deliveryStatus) {
            //배송상태가 배송중, 배송완료 인 경우 주문취소 불가
            //배송상태가 취소완료인 경우 주문취소 불가
            case BEING_DELIVERED:
            case DELIVERY_COMPLETED:
                throw new OrderCancellationNotAllowed("이미 배송 진행 중으로 취소 할 수 없습니다.");
            case CANCEL_COMPLETED:
                throw new OrderCancellationNotAllowed("이미 취소 완료된 주문입니다.");
            case PAYMENT_COMPLETED:
                this.deliveryStatus = DeliveryStatus.CANCEL_COMPLETED;
        }
    }

    //OrderedBook 추가
    public void addOrderedBook(OrderedBook orderedBook) {
        this.orderedBookList.add(orderedBook);
    }
}