package com.spock.test_demo.service

import com.spock.test_demo.common.dto.member.MemberDto
import com.spock.test_demo.exception.custom.DuplicationException
import com.spock.test_demo.exception.custom.ResourceNotFoundException
import com.spock.test_demo.repository.MemberRepository
import spock.lang.Specification

class MemberServiceTest extends Specification {

    def memberRepository = Mock(MemberRepository.class)
    def memberService = new MemberServiceImpl(memberRepository)

    def "join - 회원가입 중복시 예외"() {
        given:
        MemberDto request = MemberDto.builder()
                .email("hello@naver.com")
                .name("hello")
                .password("1234")
                .phoneNumber("010-1234-4567")
                .build()
        memberRepository.existsByEmail(_ as String) >> true

        when:
        memberService.join(request)

        then:
        def e = thrown(DuplicationException)
        e.message == "중복 예외"

    }

    def "join - 입력값이 null 인 경우"() {
        given:
        memberRepository.existsByEmail(_ as String) >> false;

        when:
        memberService.join(null)

        then:
        thrown(NullPointerException)
    }

    def "findMemberByEmail - 해당 email 이 존재하지 않는 경우"() {
        given:
        def email = "hello"
        memberRepository.findByEmail(email) >> Optional.empty()

        when:
        memberService.findMemberByEmail(email)

        then:
        def e = thrown(ResourceNotFoundException)
        e.message == "회원 이름을 찾을 수 없습니다"
    }
}
