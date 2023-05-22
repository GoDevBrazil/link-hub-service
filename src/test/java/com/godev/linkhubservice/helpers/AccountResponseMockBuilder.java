package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.AccountResponse;

import java.time.OffsetDateTime;

public class AccountResponseMockBuilder {

    private final AccountResponse accountResponse;

    public AccountResponseMockBuilder() {
        this.accountResponse = new AccountResponse();
    }

    public static AccountResponseMockBuilder getBuilder() {
        return new AccountResponseMockBuilder();
    }

    public AccountResponseMockBuilder mock() {

        this.accountResponse.setId(1);
        this.accountResponse.setName("Kibe");
        this.accountResponse.setEmail("kibe@email.com");
        this.accountResponse.setCreatedAt(OffsetDateTime.parse("2023-05-15T22:07:00Z"));
        this.accountResponse.setUpdatedAt(OffsetDateTime.parse("2023-05-15T22:07:00Z"));

        return this;
    }

    public AccountResponse build() {
        return accountResponse;
    }

}
