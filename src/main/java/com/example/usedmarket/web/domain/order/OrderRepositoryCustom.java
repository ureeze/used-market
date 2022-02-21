package com.example.usedmarket.web.domain.order;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findByUserId(Long userId);
}
