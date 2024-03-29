package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.enums.AccountFields;
import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AuthRequest;
import com.godev.linkhubservice.domain.vo.UpdateAccountRequest;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AccountResponseMockBuilder;
import com.godev.linkhubservice.helpers.AuthRequestMockBuilder;
import com.godev.linkhubservice.helpers.AuthResponseMockBuilder;
import com.godev.linkhubservice.helpers.UpdateAccountRequestMockBuilder;
import com.godev.linkhubservice.security.jwt.JwtService;
import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DateTimeException;
import java.util.List;
import java.util.stream.Stream;

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

    @BeforeEach
    void setup(){
        final var token = "kibe";
        final var userDetails = User.builder().username("kibe@email.com").password("123").roles("USER").build();

        Mockito.when(this.jwtService.isValidToken(token)).thenReturn(Boolean.TRUE);
        Mockito.when(this.jwtService.getLoggedAccount(token)).thenReturn("kibe@email.com");
        Mockito.when(this.accountService.loadUserByUsername("kibe@email.com")).thenReturn(userDetails);
    }

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

    @ParameterizedTest(name = "Should throw BadRequestException when invalid {2} is passed")
    @MethodSource("accountRequestsInvalidOrNullFields")
    void registerNullOrInvalidFields(AccountRequest accountRequest, Issue issue, AccountFields accountField) throws Exception{

        mockMvc.perform(post("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(accountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
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

    @ParameterizedTest(name = "Should throw BadRequestException when auth request has invalid {2}")
    @MethodSource("nullOrInvalidAuthRequestsAndIssues")
    void authRequestNullOrInvalidFields(AuthRequest authRequest, Issue issue, AccountFields accountField) throws Exception{

        mockMvc.perform(post("/account/auth")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
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

    @ParameterizedTest(name = "Should update account with same {1} when update account request {1} field is null")
    @MethodSource("updateAccountRequestNullFields")
    void accountUpdateNullFields(UpdateAccountRequest updateAccountRequest, AccountFields accountField) throws Exception {

        final var accountResponse = AccountResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.accountService.update(updateAccountRequest)).thenReturn(accountResponse);

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateAccountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accountResponse)));
    }

    @ParameterizedTest(name = "Should throw bad request when update account request {2} field has invalid length or format")
    @MethodSource("updateAccountRequestInvalidFormatOrLength")
    void updateAccountInvalidFormatOrLength(UpdateAccountRequest updateAccountRequest, Issue issue, AccountFields accountField) throws Exception {

        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/account")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateAccountRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    private static Stream<Arguments> accountRequestsInvalidOrNullFields() {
        return Stream.of(
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withNullName().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_REQUIRED_ERROR), AccountFields.NAME),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthName().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR), AccountFields.NAME),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withEmptyName().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, List.of(NAME_REQUIRED_ERROR, NAME_LENGTH_ERROR)), AccountFields.NAME),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withNullEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR), AccountFields.EMAIL),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR), AccountFields.EMAIL),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR), AccountFields.EMAIL),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withNullPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR), AccountFields.PASSWORD),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withInvalidPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD),
                Arguments.of(AccountRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD)
        );
    }

    private static Stream<Arguments> nullOrInvalidAuthRequestsAndIssues() {
        return Stream.of(
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withNullEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_REQUIRED_ERROR), AccountFields.EMAIL),
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withInvalidEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR), AccountFields.EMAIL),
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR), AccountFields.EMAIL),
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withNullPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_REQUIRED_ERROR), AccountFields.PASSWORD),
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withInvalidPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD),
                Arguments.of(AuthRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD)
        );
    }

    private static Stream<Arguments> updateAccountRequestNullFields() {
        return Stream.of(
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withNullName().build(), AccountFields.NAME),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withNullEmail().build(), AccountFields.EMAIL),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withNullPassword().build(), AccountFields.PASSWORD)
        );
    }

    private static Stream<Arguments> updateAccountRequestInvalidFormatOrLength() {
        return Stream.of(
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withInvalidLengthName().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR), AccountFields.NAME),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withEmptyName().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, NAME_LENGTH_ERROR), AccountFields.NAME),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withInvalidEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_FORMAT_ERROR), AccountFields.EMAIL),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withInvalidLengthEmail().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, EMAIL_LENGTH_ERROR), AccountFields.EMAIL),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withInvalidPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD),
                Arguments.of(UpdateAccountRequestMockBuilder.getBuilder().mock().withInvalidLengthPassword().build(), new Issue(IssueEnum.ARGUMENT_NOT_VALID, PASSWORD_FORMAT_ERROR), AccountFields.PASSWORD)
        );
    }
}