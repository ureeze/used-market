package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.post.QPost;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostDetailsResponseDto;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.exception.PostNotFoundException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import com.example.usedmarket.web.security.dto.LoginUser;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postsRepository;
    private final UserRepository userRepository;

    /*
     * POST 등록
     * @param userPrincipal - 현재 사용자
     * @param requestDto - 포스트등록 내용
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostResponseDto save(@LoginUser UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) {
        // UserEntity 조회
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // UserEntity 와 requestDto 를 이용해 POST 와 Book 생성
        Book book = requestDTO.toBook();
        Post post = requestDTO.toPost(userEntity);
        post.addBook(book);
        book.addPost(post);

        // PostRepository 에 POST 저장
        postsRepository.save(post);

        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(post);
    }

    /*
     * POST ID로 포스트 조회
     * @param postId - 찾고자 하는 POST ID 값
     * @return findPOST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public PostDetailsResponseDto findById(Long postId) {
        // POST 의 ID로 PostRepository 에서  POST 조회
        Post findPost = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // POST 를 PostResponseDto 로 반환
        return PostDetailsResponseDto.toResponseDto(findPost);
    }

    /*
     * POST TITLE 로 포스트 조회
     * @param postTitle - 검색하고자 하는 POST TITLE 값
     * @return findPOST 를 PostResponseDto 로 변환 후 반환
     */
    @Override
    public List<PostResponseDto> findByPostTitle(String postTitle) {
        List<Post> postList = postsRepository.findByPostTitle(postTitle);
        return postList.stream().map(post -> PostResponseDto.toResponseDto(post)).collect(Collectors.toList());
    }

    /*
     * 전체 POST 조회
     * @return findPOST 를 stream 을 이용해 PostResponseDto 로 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> findAll() {
        return postsRepository.findAll().stream().map(post -> PostResponseDto.toResponseDto(post)).collect(Collectors.toList());
    }

    /*
     * POST 수정
     * @param id - 수정하고자 하는 POST 의 ID 값
     * @param userPrincipal - 현재 사용자
     * @param requestDto - 수정하고자 하는 요청 정보
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostResponseDto updatePost(Long postId, @LoginUser UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) {
        // POST 의 ID로 POST 조회
        Post post = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // 수정요청 사용자 ID 와 POST 작성자 ID 확인
        if (userPrincipal.getId() != post.getUserEntity().getId()) {
            throw new IllegalArgumentException("사용자 ID 불일치로 수정할 수 없습니다.");
        }

        // POST 수정
        post.update(requestDTO);

        // POST 와 연관된 책 수정
        post.updateBook(requestDTO);

        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(post);
    }

    /*
     * POST 의 ID 을 이용해 POST 삭제
     * @param id - 삭제하고자 하는 POST 의 ID 값
     * @param userPrincipal - 현재 사용자
     */
    @Transactional
    @Override
    public void delete(Long postId, @LoginUser UserPrincipal userPrincipal) {
        // POST ID로 POST 조회
        Post post = postsRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // 수정요청 사용자 ID 와 POST 작성자 ID 확인
        if (userPrincipal.getId() != post.getUserEntity().getId()) {
            throw new IllegalArgumentException("사용자 ID 불일치로 삭제할 수 없습니다.");
        }

        // POST 및 BOOK 삭제
        if (post.isDeletable(userPrincipal.getId())) {
            post.deleted();
            post.getBookList().get(0).deleted();
        } else {
            throw new IllegalArgumentException("POST 를 삭제할 수 없습니다.");
        }

    }
}
