package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;
import com.example.usedmarket.web.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    //책 상세 조회
    @GetMapping("/books/{id}")
    public ResponseEntity<BookSearchListResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
    }

    //판매중인 도서 조회
    @GetMapping("/books/all/sell")
    ResponseEntity<List<BookDetailsResponseDto>> findByStatusIsSell() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByStatusIsSell());
    }

    //등록된 도서 전체 조회
    @GetMapping("/books/all")
    ResponseEntity<List<BookDetailsResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    //도서 제목 검색
    @GetMapping("/books/all/title")
    ResponseEntity<List<BookDetailsResponseDto>> findByBookTitle(@RequestParam String bookTitle) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findByBookTitle(bookTitle));
    }
}
