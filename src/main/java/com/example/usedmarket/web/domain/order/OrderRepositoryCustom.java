package com.example.usedmarket.web.domain.order;

import java.util.List;

public interface OrderRepositoryCustom {
    // USER ID 에 의한 주문 조회
    List<Order> findByUserId(Long userId);
}
