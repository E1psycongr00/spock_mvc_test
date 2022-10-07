package com.spock.test_demo.service;

import com.spock.test_demo.common.dto.member.MemberDto;
import com.spock.test_demo.entity.MemberEntity;
import com.spock.test_demo.exception.custom.DuplicationException;
import com.spock.test_demo.exception.custom.ResourceNotFoundException;
import com.spock.test_demo.mapper.MemberMapper;
import com.spock.test_demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;


    @Override
    public MemberDto join(MemberDto memberDto) throws DuplicationException{
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicationException("중복 예외");
        }
        MemberEntity memberEntity = MemberMapper.instance.convertToEntity(memberDto);
        MemberEntity savedEntity = memberRepository.save(memberEntity);
        return MemberMapper.instance.convertToDto(savedEntity);
    }

    @Override
    public MemberDto findMemberByEmail(String email) throws NotFoundException {
        MemberEntity entity = memberRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("회원 이름을 찾을 수 없습니다"));
        return MemberMapper.instance.convertToDto(entity);
    }
}
