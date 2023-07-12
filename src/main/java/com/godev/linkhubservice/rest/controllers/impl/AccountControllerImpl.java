package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.InvalidJwtException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.domain.vo.AuthRequest;
import com.godev.linkhubservice.domain.vo.AuthResponse;
import com.godev.linkhubservice.rest.controllers.AccountController;
import com.godev.linkhubservice.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.godev.linkhubservice.domain.constants.IssueDetails.GENERATE_AUTH_TOKEN_ERROR;

@RestController
@RequestMapping(value = "/account")
public class AccountControllerImpl implements AccountController {

    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountControllerImpl(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<AccountResponse> register(AccountRequest accountRequest) {
        var accountResponse = accountService.register(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }

    @Override
    public ResponseEntity<AuthResponse> auth(AuthRequest authRequest) {
        try {
            accountService.auth(authRequest);

            var token = jwtService.generateToken(authRequest.getEmail());

            var tokenResponse = AuthResponse
                    .builder()
                    .withType("Bearer")
                    .withToken(token)
                    .build();
            return ResponseEntity.ok(tokenResponse);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidJwtException(
                    new Issue(IssueEnum.HEADER_REQUIRED_ERROR, GENERATE_AUTH_TOKEN_ERROR)
            );
        }
    }
}
