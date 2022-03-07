package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;
import com.example.usedmarket.web.service.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 책 상세 조회
    @GetMapping("/books/{id}")
    public ResponseEntity<BookSearchListResponseDto> findById(@PathVariable Long id) {
        log.info("책 상세 조회");
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
    }

    // 판매 중인 도서 목록 조회
    @GetMapping("/books/all/sell")
    ResponseEntity<List<BookDetailsResponseDto>> findByStatusIsSell() {
        log.info("판매 중인 도서목록 조회");
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByStatusIsSell());
    }

    // 등록 된 도서 전체 조회
    @GetMapping("/books/all")
    ResponseEntity<List<BookDetailsResponseDto>> findAll() {
        log.info("등록 된 도서 전체 조회");
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    // 도서 제목에 의한 도서 목록 조회
    @GetMapping("/books/all/title")
    ResponseEntity<Page<BookDetailsResponseDto>> findByBookTitle(@RequestParam("title") String bookTitle, Pageable pageable) {
        log.info("도서 제목에 의한 도서 목록 조회");
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByBookTitle(bookTitle, pageable));
    }

}
