package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.repository.member.Member;
import com.example.usedmarket.web.repository.member.MemberRepository;
import com.example.usedmarket.web.repository.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    /*
    SignUpRequestDto 를 통한 member 생성.
    @return member를 memberResponseDto로 변환 후 반환
     */
    @Transactional
    @Override
    public MemberResponseDto createMember(MemberRequestDto dto) {
        //SignUpRequestDto -> member 생성.
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .role(Role.GUEST)
                .createAt(LocalDateTime.now())
                .build();
        //member를 DB에 저장.
        Member savedMember = memberRepository.save(member);
        //savedMember를 memberResponseDto에 넣어 반환
        return MemberResponseDto.toDto(savedMember);
    }


    @Override
    public MemberResponseDto findById(Long id) {
        Member updatedMember = memberRepository.findById(id).get();
        return MemberResponseDto.toDto(updatedMember);
    }

    @Override
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream().map(member -> MemberResponseDto.toDto(member)).collect(Collectors.toList());
    }

    @Override
    public Long update(Long id, MemberRequestDto requestDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
