package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.UpdateAccountResponse;

import java.time.OffsetDateTime;

public class UpdateAccountResponseMockBuilder {

    private final UpdateAccountResponse updateAccountResponse;

    public UpdateAccountResponseMockBuilder() {
        this.updateAccountResponse = new UpdateAccountResponse();
    }

    public static UpdateAccountResponseMockBuilder getBuilder() {
        return new UpdateAccountResponseMockBuilder();
    }

    public UpdateAccountResponseMockBuilder mock() {

        this.updateAccountResponse.setId(1);
        this.updateAccountResponse.setName("Kibe");
        this.updateAccountResponse.setEmail("kibe@email.com");
        this.updateAccountResponse.setUpdatedAt(OffsetDateTime.parse("2023-05-15T22:07:00Z"));

        return this;
    }

    public UpdateAccountResponse build() {
        return updateAccountResponse;
    }

}
