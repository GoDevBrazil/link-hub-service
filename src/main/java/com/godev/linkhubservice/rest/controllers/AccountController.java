package com.godev.linkhubservice.rest.controllers;

import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.domain.vo.AuthRequest;
import com.godev.linkhubservice.domain.vo.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Tag(name = "Accounts", description = "Make Account operations")
public interface AccountController {

    @Operation(description = "Register an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return an account recently registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    ResponseEntity<AccountResponse> register(@Valid @RequestBody AccountRequest accountRequest);

    @Operation(description = "Authenticate an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a Bearer token with authenticated account"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping(value = "/auth")
    ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest authRequest);
}
