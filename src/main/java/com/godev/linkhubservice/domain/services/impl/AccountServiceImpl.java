package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResponse register(AccountRequest accountRequest) {

        if(this.accountExists(accountRequest.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        var account = Account
                .builder()
                .withName(accountRequest.getName())
                .withEmail(accountRequest.getEmail())
                .withPassword(accountRequest.getPassword())
                .withCreatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .withUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        var accountSaved = accountRepository.save(account);

        return AccountResponse
                .builder()
                .withId(accountSaved.getId())
                .withName(accountSaved.getName())
                .withEmail(accountSaved.getEmail())
                .withCreatedAt(accountSaved.getCreatedAt())
                .build();
    }

    private boolean accountExists(String email) {
        var account = accountRepository.findByEmail(email).orElse(null);
        return ObjectUtils.isNotEmpty(account);
    }
}
