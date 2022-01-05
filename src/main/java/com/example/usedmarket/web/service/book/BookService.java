package com.example.usedmarket.web.service.book;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;

import java.util.List;

public interface BookService {

    //BOOK ID로 BOOK 상세 조회
    BookSearchListResponseDto findById(Long bookId);

    //판매중인 BOOK 조회
    List<BookDetailsResponseDto> findByStatusIsSell();

    //등록된 BOOK 전체 조회
    List<BookDetailsResponseDto> findAll();

    //BOOK 제목 검색
    List<BookDetailsResponseDto> findByBookTitle(String bookTitle);
}
