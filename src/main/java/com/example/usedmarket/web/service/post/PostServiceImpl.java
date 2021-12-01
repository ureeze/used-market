package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.config.auth.dto.SessionMember;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.post.Post;
import com.example.usedmarket.web.domain.post.PostRepository;
import com.example.usedmarket.web.dto.PostResponseDto;
import com.example.usedmarket.web.dto.PostSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PostResponseDto save(SessionMember sessionMember, PostSaveRequestDto requestDTO) {
        Post savedPost =postRepository.save(Post.toEntity(Member.toMember(sessionMember), requestDTO));
        return PostResponseDto.toDto(savedPost);
    }

    @Override
    public PostResponseDto findById(Long id) {
        return null;
    }

    @Override
    public List<PostResponseDto> findAll() {
        return null;
    }

    @Override
    public PostResponseDto update(Long id, PostSaveRequestDto requestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
