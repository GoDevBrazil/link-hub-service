package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.domain.vo.UpdateAccountRequest;
import com.godev.linkhubservice.helpers.AccountMockBuilder;
import com.godev.linkhubservice.helpers.AccountRequestMockBuilder;
import com.godev.linkhubservice.helpers.AuthRequestMockBuilder;
import com.godev.linkhubservice.helpers.UpdateAccountRequestMockBuilder;
import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.godev.linkhubservice.domain.constants.IssueDetails.EMAIL_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.EMAIL_NOT_FOUND_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.INVALID_CREDENTIALS_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AccountServiceImpl accountService;

    UpdateAccountRequest mockedUpdateAccountRequest;

    Account mockedAccount;

    Account mockedUpdatedAccount;

    UserDetails userDetails = User.builder()
            .username("kibe@email.com")
            .password("321")
            .roles("USER")
            .build();

    @Test
    @DisplayName("Should return Account Response when save success")
    void accountRegisterHappyPath() {

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
    @DisplayName("Should throw RuleViolationException when email is already registered")
    void accountRegisterFails() {

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
    void authHappyPath(){
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
    @DisplayName("Should throw BadRequestException when invalid credentials is passed")
    void authFails(){
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
        Assertions.assertEquals(INVALID_CREDENTIALS_ERROR, badRequestException.getIssue().getDetails().get(0));
    }

    @Test
    @DisplayName("Should return Account Response with ID when email is already registered")
    void findAccountByEmailHappyPath(){
        //arrange
        final var mockedEmail = "kibe@email.com";
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedEmail)).thenReturn(Optional.of(mockedAccountSaved));

        //action
        final var accountResponse = this.accountService.findByEmail(mockedEmail);

        //assertion
        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(mockedAccountSaved.getId(), accountResponse.getId());
        verify(this.accountRepository, times(1)).findByEmail(mockedEmail);
    }

    @Test
    @DisplayName("Should throw ObjectNotFoundException when email isn't found")
    void findAccountByEmailFails(){
        //arrange
        final var mockedEmail = "kibe@email.com";

        when(this.accountRepository.findByEmail(mockedEmail)).thenReturn(Optional.empty());

        //action
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.findByEmail(mockedEmail));

        //assertion
        Assertions.assertEquals(IssueEnum.OBJECT_NOT_FOUND.getMessage(), objectNotFoundException.getIssue().getMessage());
        Assertions.assertEquals(String.format(EMAIL_NOT_FOUND_ERROR, mockedEmail), objectNotFoundException.getIssue().getDetails().get(0));
    }

    @Test
    @DisplayName("Should return User Details when valid credentials is passed in user authentication")
    void loadUserByUsernameHappyPath(){
        //arrange
        final var mockedEmail = "kibe@email.com";
        final var mockedAccountSaved = AccountMockBuilder.getBuilder().mock().withId().build();

        when(this.accountRepository.findByEmail(mockedEmail)).thenReturn(Optional.of(mockedAccountSaved));

        //action
        final var userDetails = this.accountService.loadUserByUsername(mockedAccountSaved.getEmail());

        //assertion
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(mockedAccountSaved.getEmail(), userDetails.getUsername());
        verify(this.accountRepository, times(1)).findByEmail(mockedAccountSaved.getEmail());
    }

    @Test
    @DisplayName("Should throw ObjectNotFoundException when email isn't found in user authentication")
    void loadUserByUsernameFails(){
        //arrange
        final var mockedEmail = "kibe@email.com";

        when(this.accountRepository.findByEmail(mockedEmail)).thenReturn(Optional.empty());

        //action
        ObjectNotFoundException objectNotFoundException = assertThrows(ObjectNotFoundException.class,
                () -> this.accountService.loadUserByUsername(mockedEmail));

        //assertions
        Assertions.assertEquals(IssueEnum.OBJECT_NOT_FOUND.getMessage(), objectNotFoundException.getIssue().getMessage());
        Assertions.assertEquals(String.format(EMAIL_NOT_FOUND_ERROR, mockedEmail), objectNotFoundException.getIssue().getDetails().get(0));
    }

    @BeforeEach
    void setup(){
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(this.userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mockedUpdateAccountRequest = UpdateAccountRequestMockBuilder.getBuilder().mock().build();
        this.mockedAccount = AccountMockBuilder.getBuilder().mock().build();
        this.mockedUpdatedAccount = AccountMockBuilder.getBuilder().mock().build();
    }

    @Test
    @DisplayName("Should update account when account request valid body is passed")
    void accountUpdateHappyPath(){
        when(this.mockedAccount.)
    }
}