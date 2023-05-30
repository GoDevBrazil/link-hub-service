package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.AuthResponse;

public class AuthResponseMockBuilder {

    private final AuthResponse authResponse;

    public AuthResponseMockBuilder() {
        this.authResponse = new AuthResponse();
    }

    public static AuthResponseMockBuilder getBuilder() {
        return new AuthResponseMockBuilder();
    }

    public AuthResponseMockBuilder mock() {

        this.authResponse.setType("Bearer");
        this.authResponse.setToken("Mocked token");

        return this;
    }

    public AuthResponse build() {
        return authResponse;
    }

}
