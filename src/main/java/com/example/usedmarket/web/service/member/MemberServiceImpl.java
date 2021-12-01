package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .role(Role.USER)
                .build();
        //member를 DB에 저장.
        Member savedMember = memberRepository.save(member);
        //savedMember를 memberResponseDto에 넣어 반환
        return MemberResponseDto.toDto(savedMember);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberResponseDto findById(Long id) {
        Member updatedMember = memberRepository.findById(id).get();
        return MemberResponseDto.toDto(updatedMember);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream().map(member -> MemberResponseDto.toDto(member)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MemberResponseDto update(Long id, MemberRequestDto requestDto) {
        Member member = memberRepository.getById(id);
        member.update(requestDto.getName(), requestDto.getEmail());
        return MemberResponseDto.toDto(memberRepository.save(member));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
