package com.example.usedmarket.web.service.post;

import com.example.usedmarket.web.dto.PostRequestDto;
import com.example.usedmarket.web.dto.PostResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostService {
    @Override
    public PostResponseDto save(PostRequestDto requestDTO) {


        return null;
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
    public PostResponseDto update(Long id, PostRequestDto requestDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
