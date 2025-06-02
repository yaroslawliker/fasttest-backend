package org.yarek.fasttest.spring.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.yarek.fasttest.spring.backend.config.SecurityConfig;
import org.yarek.fasttest.spring.backend.dto.LoginRequest;
import org.yarek.fasttest.spring.backend.entities.User;
import org.yarek.fasttest.spring.backend.services.TokenService;
import org.yarek.fasttest.spring.backend.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({HomeController.class, LoginController.class})
@Import({SecurityConfig.class, TokenService.class})
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void rootWhenUnauthenticatedThen401() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rootWhenAuthenticatedThenSayHello() throws Exception {
        // Створюємо об'єкт LoginRequest
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("password");

        // Мокуємо поведінку UserService
        User mockUser = new User();
        mockUser.setUsername("test");
        mockUser.setPassword("password");
        mockUser.setRole(User.Role.STUDENT);
        when(userService.getByUsername("test")).thenReturn(mockUser);

        MvcResult result = this.mockMvc.perform(
                        post("/token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getContentAsString();

        this.mockMvc.perform(get("/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(content().string("Hello test"));
    }


    @Test
    @WithMockUser
    public void rootWhenMockUserStatusIsOk() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk());
    }



}