package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.domain.user.UserEntity;
import com.example.usedmarket.web.domain.user.UserRepository;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import com.example.usedmarket.web.dto.PostSaveResponseDto;
import com.example.usedmarket.web.security.dto.UserPrincipal;
import com.example.usedmarket.web.exception.PostNotFoundException;
import com.example.usedmarket.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postsRepository;
    private final UserRepository userRepository;

    /*
     * SessionMember 와 PostResponseDto 를 통한 POST 생성.
     * @param sessionMember 현재 세션유저
     * @param requestDto 포스트등록 내용
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostSaveResponseDto save(UserPrincipal userPrincipal, PostSaveRequestDto requestDTO) {
        //Member 조회
        UserEntity userEntity = userRepository.findByEmail(userPrincipal.getEmail()).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

        // Member 와 requestDto 를 이용해 POST 와 Book 생성
        Book book = requestDTO.toBook();
        Post post = requestDTO.toPost(userEntity, book);

        // PostRepository 에 POST 저장
        Post savedPost = postsRepository.save(post);

        // POST 를 PostResponseDto 로 반환
        return PostSaveResponseDto.toResponseDto(savedPost);
    }

    /*
     * POST 조회
     * @param id - 찾고자 하는 POST ID 값
     * @return findPOST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public PostSaveResponseDto findById(Long id) {
        // POST id로 PostRepository 에서  POST 조회
        Post findPost = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // POST 를 PostResponseDto 로 반환
        return PostSaveResponseDto.toResponseDto(findPost);
    }

    /*
     * 전체 POST 조회
     * @return findPOST 를 stream 을 이용해 PostResponseDto 로 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<PostSaveResponseDto> findAll() {
        return postsRepository.findAll().stream().map(post -> PostSaveResponseDto.toResponseDto(post)).collect(Collectors.toList());
    }

    /*
     * POST 의 id 값과 수정하고자 하는 PostSaveRequestDto 값을 이용해 POST 수정
     * @param id - 수정하고자 하는 POST 의 ID 값
     * @param requestDto - 수정하고자 하는 요청 정보
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostSaveResponseDto update(Long id, PostSaveRequestDto requestDTO) {
        // POST id로 POST 조회
        Post post = postsRepository.findById(id).orElseThrow(() -> new PostNotFoundException("POST 가 존재하지 않습니다."));

        // POST 수정
        post.update(requestDTO);

        // POST 와 연관된 책 수정
        post.getBookList().get(0).update(requestDTO);

        // POST 를 PostResponseDto 로 반환
        return PostSaveResponseDto.toResponseDto(post);
    }

    /*
     * POST 의 id 을 이용해 POST 삭제
     * @param id - 삭제하고자 하는 POST 의 ID 값
     */
    @Transactional
    @Override
    public void delete(Long id) {
        postsRepository.deleteById(id);
    }
}
