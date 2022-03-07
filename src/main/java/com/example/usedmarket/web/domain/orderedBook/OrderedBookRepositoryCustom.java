package com.example.usedmarket.web.domain.orderedBook;

import java.util.List;

public interface OrderedBookRepositoryCustom {
    // 현재 사용자에 대한 ORDERED BOOK 조회
    List<OrderedBook> findByCurrentUser(Long userId);
}
