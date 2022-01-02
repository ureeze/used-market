package com.example.usedmarket.web.service.book;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    //책 상세 조회
    BookDetailsResponseDto findById(Long bookId);

    //판매중인 도서 조회
    List<BookResponseDto> findByStatusIsSell();

    //등록된 도서 전체 조회
    List<BookResponseDto> findAll();

    //도서 제목 검색
    List<BookResponseDto> findByBookTitle(String bookTitle);
}
