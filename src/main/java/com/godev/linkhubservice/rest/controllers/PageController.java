package com.godev.linkhubservice.rest.controllers;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.PageViewRequest;
import com.godev.linkhubservice.domain.vo.PageViewResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Pages", description = "Make Page operations")
public interface PageController {

    @Operation(description = "Register a page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return a page recently registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    ResponseEntity<PageResponse> create(@Valid @RequestBody CreatePageRequest createPageRequest);

    @Operation(description = "Edit a page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a page recently updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping(value = "/{id}")
    ResponseEntity<PageResponse> update(@Valid @RequestBody UpdatePageRequest updatePageRequest, @PathVariable Integer id);

    @Operation(description = "List all pages of an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return an user pages list")
    })
    @GetMapping
    ResponseEntity<List<PageResponse>> findPagesByAccountId();

    @Operation(description = "Get a page of an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return an user page"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping(value = "/{id}")
    ResponseEntity<PageResponse> findById(@PathVariable Integer id);

    @Operation(description = "Delete a page of an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id);

    @Operation(description = "Register or increments a page view")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return date and total page views"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping(value = "/view")
    ResponseEntity<PageViewResponse> pageViewCounter(@Valid @RequestBody PageViewRequest pageViewRequest);

}
