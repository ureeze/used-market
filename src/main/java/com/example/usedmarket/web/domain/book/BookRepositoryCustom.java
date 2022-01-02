package com.example.usedmarket.web.domain.book;

import java.util.List;

public interface BookRepositoryCustom {
    List<Book> findByBookTitle(String bookTitle);

}
