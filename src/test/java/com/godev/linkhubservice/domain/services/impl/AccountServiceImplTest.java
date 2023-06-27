package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.helpers.AccountMockBuilder;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AuthRequestMockBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.godev.linkhubservice.domain.constants.IssueDetails.EMAIL_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.INVALID_CREDENTIALS_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Should return Account Response when save success")
    void accountHappyPath() {

        final var mockedAccountRequest = AccountRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.save(any())).thenReturn(mockedAccountSaved);
        when(this.accountRepository.findByEmail(mockedAccountRequest.getEmail())).thenReturn(Optional.empty());

        final var accountResponse = this.accountService.register(mockedAccountRequest);

        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(mockedAccountSaved.getId(), accountResponse.getId());
        verify(this.accountRepository, times(1)).save(any());
        verify(this.accountRepository, times(1)).findByEmail(mockedAccountRequest.getEmail());

    }

    @Test
    @DisplayName("Should throw exception when email is already registered")
    void emailAlreadyRegistered() {

        final var mockedAccountRequest = AccountRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedAccountRequest.getEmail())).thenReturn(Optional.of(mockedAccountSaved));

        RuleViolationException ruleViolationException = assertThrows(RuleViolationException.class,
                () -> this.accountService.register(mockedAccountRequest));
        Assertions.assertEquals(IssueEnum.ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(EMAIL_EXISTS_ERROR, mockedAccountRequest.getEmail())), ruleViolationException.getIssue().getDetails());

    }

    @Test
    @DisplayName("Should return User Details when valid credentials is passed")
    void userDetailsHappyPath(){
        //arrange
        final var mockedAuthRequest = AuthRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedAuthRequest.getEmail())).thenReturn(Optional.of(mockedAccountSaved));
        when(this.passwordEncoder.matches(mockedAuthRequest.getPassword(), mockedAccountSaved.getPassword())).thenReturn(Boolean.TRUE);

        //action
        UserDetails userDetails = this.accountService.auth(mockedAuthRequest);

        //assertion
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(mockedAccountSaved.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(mockedAccountSaved.getPassword(), userDetails.getPassword());
        Assertions.assertEquals("ROLE_USER", userDetails.getAuthorities().stream().toList().get(0).toString());
    }

    @Test
    @DisplayName("Should throw bad request exception when invalid credentials is passed")
    void invalidCredentials(){
        //arrange
        final var mockedAuthRequest = AuthRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedAuthRequest.getEmail())).thenReturn(Optional.of(mockedAccountSaved));
        when(this.passwordEncoder.matches(mockedAuthRequest.getPassword(), mockedAccountSaved.getPassword())).thenReturn(Boolean.FALSE);

        //action
        BadRequestException badRequestException = assertThrows(BadRequestException.class,
                () -> this.accountService.auth(mockedAuthRequest));

        //assertion
        Assertions.assertEquals(IssueEnum.HEADER_REQUIRED_ERROR.getMessage(), badRequestException.getIssue().getMessage());
        Assertions.assertEquals(List.of(INVALID_CREDENTIALS_ERROR), badRequestException.getIssue().getDetails());
    }
}