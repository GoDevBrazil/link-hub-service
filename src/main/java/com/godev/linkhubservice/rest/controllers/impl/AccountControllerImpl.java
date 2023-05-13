package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.rest.controllers.AccountController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account/")
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;

    public AccountControllerImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<AccountResponse> register(AccountRequest accountRequest) {
        var accountResponse = accountService.register(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }
}
