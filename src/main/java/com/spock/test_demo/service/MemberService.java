package com.spock.test_demo.service;


import com.spock.test_demo.common.dto.member.MemberDto;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

public interface MemberService {

    MemberDto join(MemberDto memberDto);
    MemberDto findMemberByEmail(String email) throws NotFoundException;
}
