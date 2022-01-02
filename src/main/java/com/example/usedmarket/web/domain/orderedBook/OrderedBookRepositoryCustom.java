package com.example.usedmarket.web.domain.orderedBook;

import com.querydsl.core.Tuple;

import java.util.List;

public interface OrderedBookRepositoryCustom {
    List<OrderedBook> findByCurrentUser(Long userId);
}
