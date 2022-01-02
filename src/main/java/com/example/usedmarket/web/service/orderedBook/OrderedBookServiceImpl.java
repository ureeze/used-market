package com.example.usedmarket.web.service.orderedBook;

import com.example.usedmarket.web.domain.orderedBook.OrderedBook;
import com.example.usedmarket.web.domain.orderedBook.OrderedBookRepository;
import com.example.usedmarket.web.dto.OrderedBookDetailsResponseDto;
import com.example.usedmarket.web.exception.OrderedBookNotFoundException;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderedBookServiceImpl implements OrderedBookService {

    private final OrderedBookRepository orderedBookRepository;

    /*
    주문한 책 조회
     */
    @Override
    public OrderedBookDetailsResponseDto findOrderedBook(@LoginUser UserPrincipal userPrincipal, Long id) {
        OrderedBook orderedBook = orderedBookRepository.findById(id).orElseThrow(() -> new OrderedBookNotFoundException("주문한 책을 찾을 수 없습니다."));

        // 조회 요청 사용자 ID 와 ORDERED BOOK 의 사용자 ID 확인
        if (userPrincipal.getId() != orderedBook.getUser().getId()) {
            throw new IllegalArgumentException("사용자 ID 불일치로 수정할 수 없습니다.");
        }
        return OrderedBookDetailsResponseDto.toResponseDto(orderedBook, orderedBook.getBook());
    }

    /*
    현재 사용자가 주문한 책 목록 조회
     */
    @Override
    public List<OrderedBookDetailsResponseDto> findByCurrentUser(@LoginUser UserPrincipal userPrincipal) {
        List<OrderedBook> orderedBookList = orderedBookRepository.findByCurrentUser(userPrincipal.getId());
        return orderedBookList.stream().map(orderedBook -> OrderedBookDetailsResponseDto.toResponseDto(orderedBook, orderedBook.getBook())).collect(Collectors.toList());
    }
}
