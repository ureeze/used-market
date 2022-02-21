package com.example.usedmarket.web.domain.book;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {
    // 도서 제목에 의한 도서 목록 조회
    Page<BookDetailsResponseDto> findByBookTitle(String bookTitle, Pageable pageable);
}