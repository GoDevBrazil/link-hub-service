package com.godev.linkhubservice.domain.exceptions;

public enum IssueEnum {
    ARGUMENT_NOT_VALID("An error has occurred while validate the request body."),
    FORMAT_REQUEST_NOT_VALID("The request format is not supported.");

    private final String message;

    IssueEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
