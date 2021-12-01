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
    @return member 를 memberResponseDto 로 변환 후 반환
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
        //member 를 DB에 저장.
        Member savedMember = memberRepository.save(member);
        //savedMember 를 memberResponseDto 에 넣어 반환
        return MemberResponseDto.toDto(savedMember);
    }

    /*
    MemberRepository 에서 Member 조회
    @return findMember 를 MemberResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public MemberResponseDto findById(Long id) {
        // id 값을 이용해 MemberRepository 에서 Member 조회
        Member findMember = memberRepository.findById(id).get();
        // findMember 를 MemberResponseDto 로 변환 후 반환
        return MemberResponseDto.toDto(findMember);
    }

    /*
    Repository 에서 member 리스트 조회
    @return member 를 memberResponseDto 로 stream 을 이용해 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll().stream().map(member -> MemberResponseDto.toDto(member)).collect(Collectors.toList());
    }

    /*
    Member 의 id 값과 수정하고자 하는 MemberRequestDto 를 input 으로 받음
    @return 수정된 member 를 memberResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public MemberResponseDto update(Long id, MemberRequestDto requestDto) {
        Member member = memberRepository.getById(id);
        member.update(requestDto.getName(), requestDto.getEmail());
        return MemberResponseDto.toDto(memberRepository.save(member));
    }

    /*
    input 으로 들어온 id 값에 해당하는 Member 제거
     */
    @Transactional
    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
