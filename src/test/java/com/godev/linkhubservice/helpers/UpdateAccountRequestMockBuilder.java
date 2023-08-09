package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.AccountRequest;
import com.godev.linkhubservice.domain.vo.UpdateAccountRequest;

public class UpdateAccountRequestMockBuilder {

    private final UpdateAccountRequest updateAccountRequest;

    public UpdateAccountRequestMockBuilder() {
        this.updateAccountRequest = new UpdateAccountRequest();
    }

    public static UpdateAccountRequestMockBuilder getBuilder() {
        return new UpdateAccountRequestMockBuilder();
    }

    public UpdateAccountRequestMockBuilder mock() {

        this.updateAccountRequest.setName("Kibe");
        this.updateAccountRequest.setEmail("kibe@email.com");
        this.updateAccountRequest.setPassword("Kibe@1234");

        return this;
    }

    public UpdateAccountRequestMockBuilder withNullName() {

        this.updateAccountRequest.setName(null);

        return this;
    }

    public UpdateAccountRequestMockBuilder withEmptyName() {

        this.updateAccountRequest.setName("");

        return this;
    }

    public UpdateAccountRequestMockBuilder withInvalidLengthName() {

        this.updateAccountRequest.setName("Je");

        return this;
    }

    public UpdateAccountRequestMockBuilder withNullEmail() {

        this.updateAccountRequest.setEmail(null);

        return this;
    }

    public UpdateAccountRequestMockBuilder withInvalidEmail() {

        this.updateAccountRequest.setEmail("kibe@gmail");

        return this;
    }

    public UpdateAccountRequestMockBuilder withInvalidLengthEmail() {

        this.updateAccountRequest.setEmail("a@a.c");

        return this;
    }

    public UpdateAccountRequestMockBuilder withNullPassword() {

        this.updateAccountRequest.setPassword(null);

        return this;
    }

    public UpdateAccountRequestMockBuilder withInvalidPassword() {

        this.updateAccountRequest.setPassword("Senha12345");

        return this;
    }

    public UpdateAccountRequestMockBuilder withInvalidLengthPassword() {

        this.updateAccountRequest.setPassword("S3nh@");

        return this;
    }

    public UpdateAccountRequest build() {
        return updateAccountRequest;
    }

}
