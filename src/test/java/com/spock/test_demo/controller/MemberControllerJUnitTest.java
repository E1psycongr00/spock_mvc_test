package com.spock.test_demo.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spock.test_demo.common.dto.member.MemberDto;
import com.spock.test_demo.common.dto.member.RequestMemberDto;
import com.spock.test_demo.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(RestDocumentationExtension.class)    // JUnit5 필수
@WebMvcTest
public class MemberControllerJUnitTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberServiceImpl memberService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    void joinTest() throws Exception {
        RequestMemberDto requestMemberDto = new RequestMemberDto("hello@naver.com", "hello", "1234",
            "010-1234-5678");
        String request = objectMapper.writeValueAsString(requestMemberDto);
        Mockito.doReturn(new MemberDto(1L, "hello@naver.com", "hello", "1234", "010-1234-5678"))
            .when(memberService).join(any());

        mockMvc.perform(post("/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.phoneNumber").exists())
            .andDo(document("member-post",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("User email"),
                    fieldWithPath(("name")).description("User name"),
                    fieldWithPath(("phoneNumber")).description("phone number")
                )));
    }

    @Test
    void findTest() throws Exception {
        String email = "hello@gmail.com";
        Mockito.doReturn(new MemberDto(1L, "hello@naver.com", "hello", "1234", "010-1234-5678"))
            .when(memberService).findMemberByEmail(any());

        mockMvc.perform(get("/member/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").exists())
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.phoneNumber").exists())
            .andDo(document("member-find",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("email").description("사용자 email")
                )));

    }
}
