package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.NaverBookInfo;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.exception.PostNotFoundException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.service.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    /*
     * POST 등록
     * @param userPrincipal - 현재 사용자
     * @param requestDto - 포스트등록 내용
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @CacheEvict(key = "'postAll'", value = "PostAll")
    @Transactional
    @Override
    public PostResponseDto save(@LoginUser UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) throws ParseException {
        log.info("get Service Method Call");
        // UserEntity 조회
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // 네이버 책 정보 가져오기
        String bookTitle = requestDTO.getBookTitle();
        NaverBookInfo naverBookInfo = bookService.retrieveBookInfo(bookTitle);

        // UserEntity 와 requestDto 를 이용해 POST 와 Book 생성
        Book book = requestDTO.toBook();
        Post post = requestDTO.toPost(userEntity);
        post.addBook(book);
        book.addPost(post);
        if (naverBookInfo != null) {
            book.addImgUrl(naverBookInfo.getImage());
        }

        // PostRepository 에 POST 저장
        postsRepository.save(post);

        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(userPrincipal.getId(), post);
    }

    /*
     * POST ID로 POST 상세 조회
     * @param postId - 찾고자 하는 POST ID 값
     * @return findPOST 를 PostResponseDto 로 변환 후 반환
     */
    @Cacheable(key = "'post-'+ #postId", value = "PostDetails")
    @Transactional(readOnly = true)
    @Override
    public PostResponseDto findById(@LoginUser UserPrincipal userPrincipal, Long postId) {
        log.info("get Service Method Call");

        // POST 의 ID로 PostRepository 에서  POST 조회
        Post findPost = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(userPrincipal.getId(), findPost);
    }

    /*
     * POST TITLE 로 포스트 조회
     * @param postTitle - 검색하고자 하는 POST TITLE 값
     * @return findPOST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findByPostTitle(@LoginUser UserPrincipal userPrincipal, String postTitle, Pageable pageable) {
        return postsRepository.findByPostTitle(userPrincipal.getId(), postTitle, pageable);
    }

    /*
     * 전체 POST 조회
     * @return findPOST 를 stream 을 이용해 PostResponseDto 로 변환 후 리스트로 반환
     */
    @Cacheable(key = "'postAll'", value = "PostAll")
    @Transactional(readOnly = true)
    @Override
    public Page<PostResponseDto> findAll(@LoginUser UserPrincipal userPrincipal, Pageable pageable) {
        return postsRepository.findByNotDeletedPost(userPrincipal.getId(), pageable);
    }


    // 자신의 전체 POST 조회
    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> findByAllPostAboutMyself(@LoginUser UserPrincipal userPrincipal) {
        List<Post> postList = postsRepository.findByCurrentUser(userPrincipal.getId());
        return postList.stream().map(post -> PostResponseDto.toResponseDto(userPrincipal.getId(), post)).collect(Collectors.toList());
    }

    /*
     * POST 수정
     * @param id - 수정하고자 하는 POST 의 ID 값
     * @param userPrincipal - 현재 사용자
     * @param requestDto - 수정하고자 하는 요청 정보
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Caching(evict = {
            //전체 POST 리스트
            @CacheEvict(key = "'postAll'", value = "PostAll"),
            //POST ID로 조회
            @CacheEvict(key = "'post-'+ #postId", value = "PostDetails")
    })
    @Transactional
    @Override
    public PostResponseDto updatePost(Long postId, @LoginUser UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) {
        // POST 의 ID로 POST 조회
        Post post = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // 수정요청 사용자 ID 와 POST 작성자 ID 확인
        if (!Objects.equals(userPrincipal.getId(), post.getUserEntity().getId())) {
            throw new IllegalArgumentException("사용자 ID 불일치로 수정할 수 없습니다.");
        }

        // POST 수정
        post.update(requestDTO);

        // POST 와 연관된 책 수정
        post.updateBook(requestDTO);

        // 책 재고수량에 따른 POST 판매상태 변경
        if (post.getBookList().get(0).existStock()) {
            post.changeToStatusIsSell();
        } else {
            post.changeToSoldOut();
        }

        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(userPrincipal.getId(), post);
    }

    /*
     * POST 의 ID 을 이용해 POST 삭제
     * @param id - 삭제하고자 하는 POST 의 ID 값
     * @param userPrincipal - 현재 사용자
     */
    @Caching(evict = {
            //전체 POST 리스트
            @CacheEvict(key = "'postAll'", value = "PostAll"),
            //POST ID로 조회
            @CacheEvict(key = "'post-'+ #postId", value = "PostDetails")
    })
    @Transactional
    @Override
    public void delete(Long postId, @LoginUser UserPrincipal userPrincipal) {
        // POST ID로 POST 조회
        Post post = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // 수정요청 사용자 ID 와 POST 작성자 ID 확인
        if (!Objects.equals(userPrincipal.getId(), post.getUserEntity().getId())) {
            throw new IllegalArgumentException("사용자 ID 불일치로 삭제할 수 없습니다.");
        }

        // POST 및 BOOK 삭제
        if (post.isDeletable(userPrincipal.getId())) {
            post.deleted();
        } else {
            throw new IllegalArgumentException("POST 를 삭제할 수 없습니다.");
        }

    }
}