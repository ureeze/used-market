package com.example.usedmarket.web.service.orderedBook;

import com.example.usedmarket.web.dto.OrderedBookDetailsResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;

import java.util.List;

public interface OrderedBookService {

    //주문한 책 조회
    OrderedBookDetailsResponseDto findOrderedBook(UserPrincipal userPrincipal, Long id);

    //현재 사용자가 주문한 책 목록 조회
    List<OrderedBookDetailsResponseDto> findByCurrentUser(UserPrincipal userPrincipal);
}
