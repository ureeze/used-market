package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.security.dto.SessionMember;
import com.example.usedmarket.web.domain.book.Book;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    /*
     * SessionMember 와 PostResponseDto 를 통한 POST 생성.
     * @param sessionMember 현재 세션유저
     * @param requestDto 포스트등록 내용
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostResponseDto save(SessionMember sessionMember, PostSaveRequestDto requestDTO) {
        // SessionMember 와 requestDto 를 이용해 POST 생성
        Post post = requestDTO.toPost(sessionMember);
        // PostRepository 에 POST 저장
        Post savedPost = postRepository.save(post);
        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(savedPost);
    }

    /*
     * POST 조회
     * @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public PostResponseDto findById(Long id) {
        // POST id로 PostRepository 에서  POST 조회
        Post findPost = postRepository.findById(id).get();
        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(findPost);
    }

    /*
    전체 POST 조회
    @return POST 를 stream 을 이용해 PostResponseDto 로 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<PostResponseDto> findAll() {
        return postRepository.findAll().stream().map(post ->  PostResponseDto.toResponseDto(post)).collect(Collectors.toList());
    }

    /*
    POST 의 id 값과 수정하고자 하는 PostSaveRequestDto 값을 이용해 POST 수정
    @return POST 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostResponseDto update(Long id, PostSaveRequestDto requestDTO) {
        // POST id로 POST 조회
        Post post = postRepository.findById(id).get();
        // POST 수정
        post.update(requestDTO);
        // POST 를 PostResponseDto 로 반환
        return PostResponseDto.toResponseDto(post);
    }

    /*
    POST 의 id 을 이용해 POST 삭제
     */
    @Transactional
    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
