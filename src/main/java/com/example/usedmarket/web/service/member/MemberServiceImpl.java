package com.example.usedmarket.web.service.member;

import com.example.usedmarket.web.domain.member.Member;
import com.example.usedmarket.web.domain.member.MemberRepository;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.exception.UserNotFoundException;
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
    MemberRequestDto 를 통한 Member 생성.
    @param requestDto - 가입 요청 정보
    @return Member 를 memberResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public MemberResponseDto createMember(MemberRequestDto requestDto) {
        //MemberRequestDto -> member 생성.
        Member member = requestDto.toMember();
        //member 를 DB에 저장.
        Member savedMember = memberRepository.save(member);
        //savedMember 를 memberResponseDto 에 넣어 반환
        return MemberResponseDto.toDto(savedMember);
    }

    /*
    MemberRepository 에서 Member 조회
    @param 찾고자 하는 Member ID 값
    @return findMember 를 MemberResponseDto 로 변환 후 반환
     */
    @Transactional(readOnly = true)
    @Override
    public MemberResponseDto findById(Long id) {
        // id 값을 이용해 MemberRepository 에서 Member 조회
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Member 가 존재하지 않습니다."));

        // findMember 를 MemberResponseDto 로 변환 후 반환
        return MemberResponseDto.toDto(findMember);
    }

    /*
    Repository 에서 Member 리스트 조회
    @return Member 를 MemberResponseDto 로 stream 을 이용해 변환 후 리스트로 반환
     */
    @Transactional(readOnly = true)
    @Override
    public List<MemberResponseDto> findAll() {
        // MemberRepository 에서 Member 전체 조회
        List<Member> memberList = memberRepository.findAll();

        // Stream 을 이용해 MemberResponseDto 타입으로 변환하여 반환
        return memberList.stream().map(member -> MemberResponseDto.toDto(member)).collect(Collectors.toList());
    }

    /*
    Member 의 id 값과 수정하고자 하는 MemberRequestDto 를 input 으로 받음
    @param id - 찾고자 하는 Member ID 값
    @param requestDto - 업데이트 하고자 하는 요청 정보
    @return 수정된 Member 를 MemberResponseDto 로 변환 후 반환
     */
    @Transactional
    @Override
    public MemberResponseDto update(Long id, MemberRequestDto requestDto) {
        // id 값을 이용해 MemberRepository 에서 Member 조회
        Member member = memberRepository.getById(id);
        // RequestDto 를 이용해 Member 수정
        member.update(requestDto);
        // member 가 영속성 컨텍스트에 존재하기 때문에 바로 MemberResponseDto 로 반환
        return MemberResponseDto.toDto(member);
    }

    /*
     * input 으로 들어온 id 값에 해당하는 Member 제거
     * @param id - 삭제하고자 하는 Member 의 ID
     */
    @Transactional
    @Override
    public void delete(Long id) {
        // MemberRepository 에서 ID 에 맞는 Member 삭제
        memberRepository.deleteById(id);
    }
}
