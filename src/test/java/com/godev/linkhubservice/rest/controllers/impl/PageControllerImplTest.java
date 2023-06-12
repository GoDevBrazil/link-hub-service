package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.services.PageService;
import com.godev.linkhubservice.domain.services.impl.AccountServiceImpl;
import com.godev.linkhubservice.helpers.CreatePageRequestMockBuilder;
import com.godev.linkhubservice.helpers.PageResponseMockBuilder;
import com.godev.linkhubservice.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PageService pageService;

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    void setup(){
        final var token = "kibe";
        final var userDetails = User.builder().username("kibe@email.com").password("123").roles("USER").build();

        Mockito.when(this.jwtService.isValidToken(token)).thenReturn(Boolean.TRUE);
        Mockito.when(this.jwtService.getLoggedAccount(token)).thenReturn("kibe@email.com");
        Mockito.when(this.accountService.loadUserByUsername("kibe@email.com")).thenReturn(userDetails);
    }

    @Test
    void shouldRegisterPageWhenValidBodyIsPassed() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.create(createPageRequest)).thenReturn(pageResponse);

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }
}
