package org.yarek.fasttest.spring.backend.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yarek.fasttest.spring.backend.config.SecurityConfig;
import org.yarek.fasttest.spring.backend.services.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest({HomeController.class, LoginController.class})
@Import({SecurityConfig.class, TokenService.class})
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSayHello() throws Exception {
        MvcResult result = this.mockMvc.perform(
                post("/token").with(httpBasic("test", "password")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();


        this.mockMvc.perform(MockMvcRequestBuilders.get("/")
                .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello test"));

    }

    @Test
    @WithMockUser
    public void rootWhenMockUserStatusIsOk() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(MockMvcResultMatchers.status().isOk());
    }



}