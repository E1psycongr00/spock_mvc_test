package com.spock.test_demo.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.spock.test_demo.common.dto.member.MemberDto
import com.spock.test_demo.common.dto.member.RequestMemberDto
import com.spock.test_demo.exception.custom.DuplicationException
import com.spock.test_demo.exception.custom.ResourceNotFoundException
import com.spock.test_demo.service.MemberService
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.any
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class MemberControllerTest extends Specification {

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext ctx;

    @Autowired
    ObjectMapper objectMapper;

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))// 필터 추가
                .build();
    }

    def "join - 중복 예외 400 리턴"() {
        given:
        def requestMemberDto = new RequestMemberDto("hello@naver.com", "hello", "1234", "010-1234-5678");
        def request = objectMapper.writeValueAsString(requestMemberDto)
        Mockito.doThrow(new DuplicationException("중복 예외")).when(memberService).join(any())

        expect:
        def andReturn = mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andReturn()
        println andReturn.response.getContentAsString()
    }

    def "join - 입력 유효성 검사 통과 못하면 400 리턴"() {
        expect:
        def requestDto = new RequestMemberDto(email, name, password, phone)
        def andReturn = mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
        println andReturn.response.getContentAsString()

        where:
        email                  | name    | password | phone
        "marrin1101@naver.com" | "1"     | "1234"   | "010-1234-1234"
        "m"                    | "hello" | "1234"   | "010-1234-1234"
        "marrin1101@naver.com" | "hello" | ""       | "010-1234-1234"
        "marrin1101@naver.com" | "hello" | "    "   | "010-1234-1234"
        "          "           | "hello" | "1234"   | "010-1234-1234"
        "marrin1101@naver.com" | "hello" | "1234"   | "01234-123-23124"
    }

    def "join - 출력 형식 검증"() {
        given:
        def requestMemberDto = new RequestMemberDto("hello@naver.com", "hello", "1234", "010-1234-5678");
        def request = objectMapper.writeValueAsString(requestMemberDto)
        Mockito.doReturn(new MemberDto(1L, "hello@naver.com", "hello", "1234", "010-1234-5678"))
                .when(memberService).join(any())

        expect:
        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("\$.id").exists())
                .andExpect(jsonPath("\$.email").exists())
                .andExpect(jsonPath("\$.name").exists())
                .andExpect(jsonPath("\$.phoneNumber").exists())
    }

    def "find - 조회 실패시 404 리턴"() {
        given:
        Mockito.doThrow(new ResourceNotFoundException("회원 email을 찾을 수 없음")).when(memberService).findMemberByEmail(any())
        def email = "hello@gmail.com"

        expect:
        def andReturn = mockMvc.perform(get("/member/" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn()
        println andReturn.response.getContentAsString()
    }

    def "find - 입력 유효성 검사"() {
        setup:
        Mockito.doReturn(new MemberDto(1L, "hello@naver.com", "hello", "1234", "010-1234-5678"))
                .when(memberService).findMemberByEmail(any())

        expect:
        def email = "hello"
        def andReturn = mockMvc.perform(get("/member/" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
        println andReturn.response.getContentAsString()
    }

    def "find - 출력 형식 검증"() {
        setup:
        Mockito.doReturn(new MemberDto(1L, "hello@naver.com", "hello", "1234", "010-1234-5678"))
                .when(memberService).findMemberByEmail(any())

        expect:
        def email = "hello@gmail.com"
        def andReturn = mockMvc.perform(get("/member/" + email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("\$.id").exists())
                .andExpect(jsonPath("\$.email").exists())
                .andExpect(jsonPath("\$.name").exists())
                .andExpect(jsonPath("\$.phoneNumber").exists())
                .andReturn()
        println andReturn.response.getContentAsString()
    }
}
