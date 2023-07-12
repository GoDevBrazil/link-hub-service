package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.services.AccountService;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AccountResponseMockBuilder;
import com.godev.linkhubservice.helpers.AuthRequestMockBuilder;
import com.godev.linkhubservice.helpers.AuthResponseMockBuilder;
import com.godev.linkhubservice.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DateTimeException;
import java.util.List;

import static com.godev.linkhubservice.domain.constants.IssueDetails.GENERATE_AUTH_TOKEN_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.INVALID_CREDENTIALS_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.NAME_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.NAME_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PASSWORD_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PASSWORD_REQUIRED_ERROR;
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
    private JwtService jwtService;

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
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_REQUIRED_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenNameFieldHasInvalidLength() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenNameFieldIsEmpty() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withEmptyName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, List.of(NAME_REQUIRED_ERROR, NAME_LENGTH_ERROR)))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestEmailFieldIsNull() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestEmailFieldIsInvalid() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestEmailFieldHasInvalidLength() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestPasswordFieldIsNull() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestPasswordFieldIsInvalid() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAccountRequestPasswordFieldHasInvalidLength() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    void shouldAuthenticateAccountWhenValidBodyIsPassed() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().build();
        final var authResponse = AuthResponseMockBuilder.getBuilder().mock().build();

        Mockito.when(this.accountService.auth(authRequest)).thenReturn(null);
        Mockito.when(this.jwtService.generateToken(authRequest.getEmail())).thenReturn(authResponse.getToken());

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authResponse)));

    }

    @Test
    void shouldThrowBadRequestExceptionWhenAuthenticationFails() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().build();
        final var exception = new BadRequestException(
                new Issue(IssueEnum.HEADER_REQUIRED_ERROR, INVALID_CREDENTIALS_ERROR));

        Mockito.when(this.accountService.auth(authRequest)).thenThrow(exception);

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(exception.getIssue())));

    }

    @Test
    void shouldThrowInvalidJwtExceptionWhenGenerateTokenFails() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().build();

        Mockito.when(this.jwtService.generateToken(authRequest.getEmail()))
                .thenThrow(new DateTimeException("Error on parsing date"));

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.HEADER_REQUIRED_ERROR, GENERATE_AUTH_TOKEN_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestEmailFieldIsNull() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withNullEmail().build();


        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestEmailFieldIsInvalid() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestEmailFieldHasInvalidLength() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestPasswordFieldIsNull() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withNullPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestPasswordFieldIsInvalid() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    void shouldThrowBadRequestWhenAuthRequestPasswordFieldHasInvalidLength() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }
}