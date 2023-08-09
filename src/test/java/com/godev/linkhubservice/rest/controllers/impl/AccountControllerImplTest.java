package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.helpers.*;
import com.godev.linkhubservice.security.jwt.JwtService;
import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DateTimeException;
import java.util.List;

import static com.godev.linkhubservice.domain.constants.IssueDetails.GENERATE_AUTH_TOKEN_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.INVALID_CREDENTIALS_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Should register account when account request valid body is passed")
    void registerHappyPath() throws Exception {

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
    @DisplayName("Should throw bad request when account request name field is null")
    void registerNullNameField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request name field has invalid length")
    void registerInvalidLengthNameField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request name field is empty")
    void registerEmptyNameField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withEmptyName().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, List.of(NAME_REQUIRED_ERROR, NAME_LENGTH_ERROR)))));

    }

    @Test
    @DisplayName("Should throw bad request when account request email field is null")
    void registerNullEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request email field is invalid")
    void registerInvalidEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request email field has invalid length")
    void registerInvalidLengthEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request password field is null")
    void registerNullPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request password field is invalid")
    void registerInvalidPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when account request password field has invalid length")
    void registerInvalidLengthPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build();

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should authenticate account when auth request valid body is passed")
    void authAccountHappyPath() throws Exception {

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
    @DisplayName("Should throw bad request exception when authentication fails")
    void authFails() throws Exception {

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
    @DisplayName("Should throw invalid jwt exception when generate token fails")
    void authGenerateTokenFails() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().build();

        Mockito.when(this.jwtService.generateToken(authRequest.getEmail()))
                .thenThrow(new DateTimeException("Error on parsing date"));

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.AUTHENTICATION_ERROR, GENERATE_AUTH_TOKEN_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request email field is null")
    void authRequestEmailFieldIsNull() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withNullEmail().build();


        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request email field is invalid")
    void authRequestEmailFieldIsInvalid() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request email field has invalid length")
    void authRequestEmailFieldHasInvalidLength() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request password field is null")
    void authRequestPasswordFieldIsNull() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withNullPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request password field is invalid")
    void authRequestPasswordFieldIsInvalid() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when auth request password field has invalid length")
    void authRequestPasswordFieldHasInvalidLength() throws Exception {

        final var authRequest = AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build();

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @BeforeEach
    void setup(){
        final var token = "kibe";
        final var userDetails = User.builder().username("kibe@email.com").password("123").roles("USER").build();

        Mockito.when(this.jwtService.isValidToken(token)).thenReturn(Boolean.TRUE);
        Mockito.when(this.jwtService.getLoggedAccount(token)).thenReturn("kibe@email.com");
        Mockito.when(this.accountService.loadUserByUsername("kibe@email.com")).thenReturn(userDetails);
    }

    @Test
    @DisplayName("Should update account when account request valid body is passed")
    void accountUpdateHappyPath() throws Exception {

        final var updateAccountRequest = UpdateAccountRequestMockBuilder.getBuilder().mock().build();
        final var accountResponse = AccountResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.accountService.update(updateAccountRequest)).thenReturn(accountResponse);

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateAccountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponse)));

    }

    @Test
    @DisplayName("Should throw bad request when update account request name field is null")
    void accountUpdateNullNameField() throws Exception {

        final var updateAccountRequest = UpdateAccountRequestMockBuilder.getBuilder().mock().withNullName().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateAccountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request name field has invalid length")
    void accountUpdateInvalidLengthNameField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthName().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request name field is empty")
    void accountUpdateEmptyNameField() throws Exception {

        final var updateAccountRequest = UpdateAccountRequestMockBuilder.getBuilder().mock().withEmptyName().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateAccountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request email field is null")
    void accountUpdateNullEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullEmail().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request email field is invalid")
    void accountUpdateInvalidEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request email field has invalid length")
    void accountUpdateInvalidLengthEmailField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request password field is null")
    void accountUpdateNullPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withNullPassword().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request password field is invalid")
    void accountUpdateInvalidPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidPassword().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }

    @Test
    @DisplayName("Should throw bad request when update account request password field has invalid length")
    void accountUpdateInvalidLengthPasswordField() throws Exception {

        final var accountRequest = AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR))));

    }
}