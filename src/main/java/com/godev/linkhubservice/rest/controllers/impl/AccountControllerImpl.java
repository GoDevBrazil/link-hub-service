package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.InvalidJwtException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.vo.*;
import com.godev.linkhubservice.rest.controllers.AccountController;
import com.godev.linkhubservice.security.jwt.JwtService;
import com.godev.linkhubservice.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.godev.linkhubservice.domain.constants.IssueDetails.GENERATE_AUTH_TOKEN_ERROR;

@RestController
@RequestMapping(value = "/account")
@Slf4j
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountControllerImpl(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<AccountResponse> register(AccountRequest accountRequest) {

        log.info("Starting register account {}", accountRequest.getName());

        var accountResponse = accountService.register(accountRequest);

        log.info("Account {} saved in database", accountRequest.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @Override
    public ResponseEntity<AuthResponse> auth(AuthRequest authRequest) {

        log.info("Authenticating account {}", authRequest.getEmail());

        try {
            accountService.auth(authRequest);

            var token = jwtService.generateToken(authRequest.getEmail());

            var tokenResponse = AuthResponse
                    .builder()
                    .withType("Bearer")
                    .withToken(token)
                    .build();

            log.info("Account {} authenticated", authRequest.getEmail());

            return ResponseEntity.ok(tokenResponse);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidJwtException(
                    new Issue(IssueEnum.AUTHENTICATION_ERROR, GENERATE_AUTH_TOKEN_ERROR)
            );
        }
    }

    @Override
    public ResponseEntity<AccountResponse> update(UpdateAccountRequest updateAccountRequest) {

        log.info("Starting update of account {}", updateAccountRequest.getName());

        var accountResponse = accountService.update(updateAccountRequest);

        log.info("Account update successful");

        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }
}
