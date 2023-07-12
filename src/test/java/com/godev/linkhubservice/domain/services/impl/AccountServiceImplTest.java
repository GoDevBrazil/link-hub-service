package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.helpers.AccountMockBuilder;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AuthRequestMockBuilder;
import org.junit.jupiter.api.Assertions;
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
    void shouldReturnAccountResponseWhenSaveSuccess() {

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
    void shouldThrowExceptionWhenEmailIsAlreadyRegistered() {

        final var mockedAccountRequest = AccountRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedAccountRequest.getEmail())).thenReturn(Optional.of(mockedAccountSaved));

        RuleViolationException ruleViolationException = assertThrows(RuleViolationException.class, () -> this.accountService.register(mockedAccountRequest));
        Assertions.assertEquals(IssueEnum.ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(EMAIL_EXISTS_ERROR, mockedAccountRequest.getEmail())), ruleViolationException.getIssue().getDetails());

    }

    @Test
    void shouldReturnUserDetailsWhenValidCredentialsIsPassed(){

        final var mockedAuthRequest = AuthRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedAuthRequest.getEmail())).thenReturn(Optional.of(mockedAccountSaved));
        when(this.passwordEncoder.matches(mockedAuthRequest.getPassword(), mockedAccountSaved.getPassword())).thenReturn(Boolean.TRUE);

        UserDetails userDetails = this.accountService.auth(mockedAuthRequest);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(mockedAccountSaved.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(mockedAccountSaved.getPassword(), userDetails.getPassword());
        Assertions.assertEquals("ROLE_USER", userDetails.getAuthorities().stream().toList().get(0).toString());
    }
}