package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.config.auth.dto.SessionMember;
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
    SessionMember 와 PostResponseDto 를 통한 post 생성.
    @return savedPost 를 PostResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public PostResponseDto save(SessionMember sessionMember, PostSaveRequestDto requestDTO) {
        // Session 를 변환한 Member 와 requestDto 를 이용해 Post 생성
        // requestDto 를 이용해 Book 생성
        Post post = Post.toPost(Member.toMember(sessionMember), requestDTO);
        Book book = Book.toBook(requestDTO);
        // post 에 book 추가
        post.getBookList().add(book);

        // PostRepository 에 post 저장
        Post savedPost = postRepository.save(post);
        return PostResponseDto.toDto(savedPost);
    }

    /*
    Post 의 id 값을 이용한 Post 조회
    @return findPost 를 PostResponseDto 로 변환 후 반환
     */
    @Override
    public PostResponseDto findById(Long id) {
        Post findPost = postRepository.findById(id).get();
        return PostResponseDto.toDto(findPost);
    }

    /*
    전체 Post 조회
    @return Post 를 PostResponseDto 로 stream 을 이용해 변환 후 리스트로 반환
     */
    @Override
    public List<PostResponseDto> findAll() {
        return postRepository.findAll().stream().map(post ->  PostResponseDto.toDto(post)).collect(Collectors.toList());
    }

    /*
    Post 의 id 값과 수정하고자 하는 PostSaveRequestDto 값을 이용해 Post 수정
    @return Post 를 PostResponseDto 로 변환 후 반환
     */
    @Override
    public PostResponseDto update(Long id, PostSaveRequestDto requestDTO) {
        Post post = postRepository.findById(id).get();
        post.update(requestDTO);
        post.getBookList().get(0).update(requestDTO);

        return PostResponseDto.toDto(post);
    }

    /*
    Post 의 id 을 이용해 Post 삭제
     */
    @Override
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
