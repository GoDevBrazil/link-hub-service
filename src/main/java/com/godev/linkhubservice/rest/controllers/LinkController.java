package com.godev.linkhubservice.rest.controllers;

import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Links", description = "Make link operations")
public interface LinkController {

    @Operation(description = "Register a link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return a link recently registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    ResponseEntity<LinkResponse> create(@Valid @RequestBody LinkRequest linkRequest);
}
