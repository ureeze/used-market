package com.example.usedmarket.web.service.book;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.book.BookRepository;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.BookDetailsResponseDto;
import com.example.usedmarket.web.dto.BookSearchListResponseDto;
import com.example.usedmarket.web.dto.NaverBookInfo;
import com.example.usedmarket.web.exception.BookNotFoundException;
import com.example.usedmarket.web.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PostRepository postRepository;

    /*
    BOOK ID 로 BOOK 상세 조회
     */
    @Override
    public BookSearchListResponseDto findById(Long bookId) {
        // ID 값으로 BOOK 조회
        Book retrieveBook = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("책이 존재하지 않습니다."));
//        Post retrievePost = postRepository.findById(retrieveBook.getPost().getId()).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));
        // DTO 로 변환 후 리턴
        return BookSearchListResponseDto.toDto(retrieveBook);
    }

    /*
    판매 중인 BOOK LIST 조회
     */
    @Override
    public List<BookDetailsResponseDto> findByStatusIsSell() {
        // 판매 상태인 POST LIST 조회
        List<Post> postList = postRepository.findByStatusIsSell();

        // 판매 중인 BOOK LIST 를 담기 위한 LIST 생성
        List<BookDetailsResponseDto> responseDtoList = new ArrayList<>();

        // LIST 에 담기
        postList.stream().forEach(post -> {
            responseDtoList.addAll(post.getBookList().stream().map(book -> BookDetailsResponseDto.toDto(book)).collect(Collectors.toList()));
        });

        // 반환
        return responseDtoList;
    }

    /*
    등록된 도서 전체 조회 (ADMIN)
     */
    @Override
    public List<BookDetailsResponseDto> findAll() {
        List<Post> postList = postRepository.findAll();
        List<BookDetailsResponseDto> responseDtoList = new ArrayList<>();
        postList.forEach(post -> {
            for (Book retrieveBook : post.getBookList()) {
                responseDtoList.add(BookDetailsResponseDto.toDto(retrieveBook));
            }
        });
        return responseDtoList;
    }

    /*
    도서 제목 검색
     */
    @Override
    public Page<BookDetailsResponseDto> findByBookTitle(String bookTitle, Pageable pageable) {
        return bookRepository.findByBookTitle(bookTitle, pageable);
    }

    @Override
    public NaverBookInfo retrieveBookInfo(String bookTitle) throws ParseException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Naver-Client-Id", "qYS6H9SHymBxVVUqIq5h");
            headers.add("X-Naver-Client-Secret", "VMyHdP2hXi");
            String url = "https://openapi.naver.com/v1/search/book.json?query=" + bookTitle + "&display=10&start=1";
            HttpEntity httpEntity = new HttpEntity(headers);
            ResponseEntity<String> s = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject body = (JSONObject) jsonParser.parse(s.getBody());
            JSONArray items = (JSONArray) body.get("items");
            JSONObject book = (JSONObject) items.get(0);
            System.out.println(book.toString());
            return NaverBookInfo.toDto(book);
        } catch (Exception e) {
            return null;
        }
    }
}
