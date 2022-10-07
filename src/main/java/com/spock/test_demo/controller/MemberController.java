package com.spock.test_demo.controller;

import com.spock.test_demo.common.dto.member.MemberDto;
import com.spock.test_demo.common.dto.member.RequestMemberDto;
import com.spock.test_demo.common.dto.member.ResponseMemberDto;
import com.spock.test_demo.mapper.MemberMapper;
import com.spock.test_demo.service.MemberService;
import javax.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ResponseMemberDto> join(@Validated @RequestBody RequestMemberDto requestMemberDto) {
        MemberDto request = MemberMapper.instance.convertToDto(requestMemberDto);
        MemberDto response = memberService.join(request);
        ResponseMemberDto responseMemberDto = MemberMapper.instance.convertToResponseDto(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMemberDto);
    }

    @GetMapping("/{email}")
    public ResponseEntity<ResponseMemberDto> findByEmail(@Email @PathVariable String email) throws NotFoundException {
        MemberDto response = memberService.findMemberByEmail(email);
        ResponseMemberDto responseMemberDto = MemberMapper.instance.convertToResponseDto(response);
        return ResponseEntity.status(HttpStatus.OK).body(responseMemberDto);
    }
}
