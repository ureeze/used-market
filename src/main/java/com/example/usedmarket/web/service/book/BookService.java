package com.example.usedmarket.web.service.book;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;
import com.example.usedmarket.web.dto.NaverBookInfo;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    //BOOK ID로 BOOK 상세 조회
    BookSearchListResponseDto findById(Long bookId);

    //판매중인 BOOK 조회
    List<BookDetailsResponseDto> findByStatusIsSell();

    //등록된 BOOK 전체 조회
    List<BookDetailsResponseDto> findAll();

    //BOOK 제목 검색
    Page<BookDetailsResponseDto> findByBookTitle(String bookTitle, Pageable pageable);

    //네이버 BOOK 정보 가져오기
    NaverBookInfo retrieveBookInfo(String bookTitle) throws ParseException;
}
