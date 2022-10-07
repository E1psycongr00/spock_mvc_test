package com.spock.test_demo.mapper;

import com.spock.test_demo.common.dto.member.MemberDto;
import com.spock.test_demo.common.dto.member.RequestMemberDto;
import com.spock.test_demo.common.dto.member.ResponseMemberDto;
import com.spock.test_demo.entity.MemberEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    MemberMapper instance = Mappers.getMapper(MemberMapper.class);

    MemberEntity convertToEntity(MemberDto memberDto);
    MemberDto convertToDto(MemberEntity memberEntity);

    MemberDto convertToDto(RequestMemberDto requestMemberDto);

    ResponseMemberDto convertToResponseDto(MemberDto memberDto);
}
