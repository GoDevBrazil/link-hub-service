package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.models.Account;

import java.time.OffsetDateTime;

public class AccountMockBuilder {

    private final Account account;

    public AccountMockBuilder() {
        this.account = new Account();
    }

    public static AccountMockBuilder getBuilder() {
        return new AccountMockBuilder();
    }

    public AccountMockBuilder mock() {

        this.account.setName("Kibe");
        this.account.setEmail("kibe@email.com");
        this.account.setPassword("Kibe@1234");
        this.account.setCreatedAt(OffsetDateTime.parse("2023-05-15T22:07:00Z"));
        this.account.setUpdatedAt(OffsetDateTime.parse("2023-05-15T22:07:00Z"));

        return this;
    }

    public AccountMockBuilder withId() {

        this.account.setId(1);

        return this;
    }

    public AccountMockBuilder withNewEmail() {

        this.account.setEmail("law@email.com");

        return this;
    }

    public AccountMockBuilder withAnotherId() {

        this.account.setId(2);

        return this;
    }

    public Account build() {
        return account;
    }

}
