package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.AuthRequest;

public class AuthRequestMockBuilder {

    private final AuthRequest authRequest;

    public AuthRequestMockBuilder() {
        this.authRequest = new AuthRequest();
    }

    public static AuthRequestMockBuilder getBuilder() {
        return new AuthRequestMockBuilder();
    }

    public AuthRequestMockBuilder mock() {

        this.authRequest.setEmail("kibe@email.com");
        this.authRequest.setPassword("Kibe@1234");

        return this;
    }

    public AuthRequestMockBuilder withNullEmail() {

        this.authRequest.setEmail(null);

        return this;
    }

    public AuthRequestMockBuilder withInvalidEmail() {

        this.authRequest.setEmail("kibe@gmail");

        return this;
    }

    public AuthRequestMockBuilder withInvalidLengthEmail() {

        this.authRequest.setEmail("a@a.c");

        return this;
    }

    public AuthRequestMockBuilder withNullPassword() {

        this.authRequest.setPassword(null);

        return this;
    }

    public AuthRequestMockBuilder withInvalidPassword() {

        this.authRequest.setPassword("Senha12345");

        return this;
    }

    public AuthRequestMockBuilder withInvalidLengthPassword() {

        this.authRequest.setPassword("S3nh@");

        return this;
    }

    public AuthRequest build() {
        return authRequest;
    }

}
