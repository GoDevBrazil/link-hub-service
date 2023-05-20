package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.AccountRequest;

public class AccountRequestMockBuilder {

    private final AccountRequest accountRequest;

    public AccountRequestMockBuilder() {
        this.accountRequest = new AccountRequest();
    }

    public static AccountRequestMockBuilder getBuilder() {
        return new AccountRequestMockBuilder();
    }

    public AccountRequestMockBuilder mock() {

        this.accountRequest.setName("Kibe");
        this.accountRequest.setEmail("kibe@email.com");
        this.accountRequest.setPassword("Kibe@1234");

        return this;
    }

    public AccountRequestMockBuilder withNullName() {

        this.accountRequest.setName(null);

        return this;
    }

    public AccountRequestMockBuilder withInvalidName() {

        this.accountRequest.setName("Peu");

        return this;
    }

    public AccountRequestMockBuilder withInvalidLengthName() {

        this.accountRequest.setName("Je");

        return this;
    }

    public AccountRequestMockBuilder withNullEmail() {

        this.accountRequest.setEmail(null);

        return this;
    }

    public AccountRequestMockBuilder withInvalidEmail() {

        this.accountRequest.setEmail("kibe@gmail");

        return this;
    }

    public AccountRequestMockBuilder withInvalidLengthEmail() {

        this.accountRequest.setEmail("a@a.c");

        return this;
    }

    public AccountRequestMockBuilder withNullPassword() {

        this.accountRequest.setPassword(null);

        return this;
    }

    public AccountRequestMockBuilder withInvalidPassword() {

        this.accountRequest.setPassword("Senha12345");

        return this;
    }

    public AccountRequestMockBuilder withInvalidLengthPassword() {

        this.accountRequest.setPassword("S3nh@");

        return this;
    }

    public AccountRequest build() {
        return accountRequest;
    }

}
