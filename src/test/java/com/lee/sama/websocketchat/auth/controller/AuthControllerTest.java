package com.lee.sama.websocketchat.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.sama.websocketchat.auth.dto.JoinRequest;
import com.lee.sama.websocketchat.auth.dto.LoginRequest;
import com.lee.sama.websocketchat.auth.repositroty.UserRepositroty;
import com.lee.sama.websocketchat.entitys.UserInfo;
import com.lee.sama.websocketchat.entitys.UserRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class AuthControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    UserRepositroty userRepositroty;
    @Autowired
    PasswordEncoder passwordEncoder;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
//                .build();


        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation).uris().withScheme("http")
                        .withHost("localhost").withPort(8080))
                .build();


        UserInfo userInfo = new UserInfo("tkdrb4807@gmail.com","리주똥",passwordEncoder.encode("1234"));
        userInfo.haveAuthority(UserRule.RULE_USER);
        userRepositroty.save(userInfo);
    }

    @Transactional
    @Test
    void signup() throws Exception {
        JoinRequest joinRequest = new JoinRequest("Lee@email.com","이주형","1234");

        this.mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(joinRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("signup", responseFields(
                        fieldWithPath("userId").description("회원 가입으로 생성된 "),
                        fieldWithPath("email").description("회원가입된 이메일"),
                        fieldWithPath("nickName").description("회원가입된 닉네임")
                )));
    }

    @Test
    void login() throws Exception {
        LoginRequest loginRequest = new LoginRequest("tkdrb4807@gmail.com","1234");

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(loginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("login", responseFields(
                        fieldWithPath("grantType").description("인증타입"),
                        fieldWithPath("accessToken").description("accessToken"),
                        fieldWithPath("refreshToken").description("refreshToken"),
                        fieldWithPath("accessTokenExpiresIn").description("회원가입된 이메일")
                )));





    }
}