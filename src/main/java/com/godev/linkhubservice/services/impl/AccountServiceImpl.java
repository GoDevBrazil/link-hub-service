package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.domain.vo.*;
import com.godev.linkhubservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.godev.linkhubservice.domain.constants.IssueDetails.EMAIL_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.EMAIL_NOT_FOUND_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.INVALID_CREDENTIALS_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.OBJECT_NOT_FOUND;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public AccountResponse register(AccountRequest accountRequest) {

        log.info("Verifying if email {} already registered", accountRequest.getEmail());

        if(this.accountExists(accountRequest.getEmail())) {
            throw new RuleViolationException(
                    new Issue(IssueEnum.ARGUMENT_NOT_VALID, String.format(EMAIL_EXISTS_ERROR, accountRequest.getEmail()))
            );
        }

        var account = Account
                .builder()
                .withName(accountRequest.getName())
                .withEmail(accountRequest.getEmail())
                .withPassword(passwordEncoder.encode(accountRequest.getPassword()))
                .withCreatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .withUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        log.info("Starting saving account {} in database", account.getName());

        var accountSaved = accountRepository.save(account);

        return AccountResponse
                .builder()
                .withId(accountSaved.getId())
                .withName(accountSaved.getName())
                .withEmail(accountSaved.getEmail())
                .withCreatedAt(accountSaved.getCreatedAt())
                .build();
    }

    @Override
    public UserDetails auth(AuthRequest authRequest) {
        var userDetails = loadUserByUsername(authRequest.getEmail());
        var passwordMatches = passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword());

        if(passwordMatches) {
            return userDetails;
        }
        throw new BadRequestException(
                new Issue(IssueEnum.HEADER_REQUIRED_ERROR, INVALID_CREDENTIALS_ERROR)
        );
    }

    @Override
    public Account findByEmail(String email) {

        log.info("Finding e-mail {}", email);

        return this.accountRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(
                        new Issue(OBJECT_NOT_FOUND, String.format(EMAIL_NOT_FOUND_ERROR, email))
                ));

    }

    @Override
    public UpdateAccountResponse update(UpdateAccountRequest updateAccountRequest) {
        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = findByEmail(userDetails.getUsername());

        log.info("Verifying if e-mail {} already registered", updateAccountRequest.getEmail());

        if(ObjectUtils.isEmpty(updateAccountRequest.getEmail())){
            updateAccountRequest.setEmail(account.getEmail());
        }

        if(!updateAccountRequest.getEmail().equals(account.getEmail()) && this.accountExists(updateAccountRequest.getEmail())) {
            throw new RuleViolationException(
                    new Issue(IssueEnum.ARGUMENT_NOT_VALID, String.format(EMAIL_EXISTS_ERROR, updateAccountRequest.getEmail()))
            );
        }

        if(ObjectUtils.isEmpty(updateAccountRequest.getName())){
            updateAccountRequest.setName(account.getName());
        }

        if(ObjectUtils.isEmpty(updateAccountRequest.getPassword())){
            updateAccountRequest.setPassword(account.getPassword());
        }

        account.setName(updateAccountRequest.getName());
        account.setEmail(updateAccountRequest.getEmail());
        account.setPassword(passwordEncoder.encode(updateAccountRequest.getPassword()));
        account.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        log.info("Saving changes in database");

        var accountUpdated = accountRepository.save(account);

        return UpdateAccountResponse
                .builder()
                .withId(accountUpdated.getId())
                .withName(accountUpdated.getName())
                .withEmail(accountUpdated.getEmail())
                .withUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();
    }

    private boolean accountExists(String email) {
        var account = accountRepository.findByEmail(email).orElse(null);
        return ObjectUtils.isNotEmpty(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException(
                        new Issue(OBJECT_NOT_FOUND, String.format(EMAIL_NOT_FOUND_ERROR, email))
                ));
                return User
                        .builder()
                        .username(account.getEmail())
                        .password(account.getPassword())
                        .roles("USER")
                        .build();
    }

}
