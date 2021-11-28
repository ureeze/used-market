package com.example.usedmarket.web.controller;

import com.example.usedmarket.web.dto.MemberResponseDto;
import com.example.usedmarket.web.dto.MemberRequestDto;
import com.example.usedmarket.web.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberResponseDto> save(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto createMember = memberService.createMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createMember);
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> findById(@PathVariable Long id) {
        MemberResponseDto findMember = memberService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(findMember);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findAll());
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> updateOne(@PathVariable Long id, @RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.update(id, memberRequestDto));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponseDto> updateOne(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(MemberResponseDto.builder().id(id).build());
    }
}
