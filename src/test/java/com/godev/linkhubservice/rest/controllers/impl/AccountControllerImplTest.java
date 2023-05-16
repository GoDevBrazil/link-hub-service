package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AccountResponseMockBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    void shouldRegisterAccountWhenValidBodyIsPassed() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().build();
        final var accountResponse = AccountResponseMockBuilder.getBuilder().mock().build();

        Mockito.when(this.accountService.register(accountRequest)).thenReturn(accountResponse);

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponse)));

    }

    @Test
    void shouldThrowBadRequestWhenNameFieldIsNull() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, "O campo nome é obrigatório!"))));

    }

    @Test
    void shouldThrowBadRequestWhenNameFieldIsInvalid() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, "O campo nome precisa ter entre 4 e 20 caracteres."))));

    }

    @Test
    void shouldThrowBadRequestWhenEmailFieldIsNull() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, "O campo email é obrigatório!"))));

    }

    @Test
    void shouldThrowBadRequestWhenEmailFieldIsInvalid() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, "O campo email é precisa ser preenchido com um e-mail no formato válido."))));

    }

    @Test
    void shouldThrowBadRequestWhenEmailFieldIsInvalidLength() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, "O campo email precisa ter entre 6 e 50 caracteres."))));

    }
}