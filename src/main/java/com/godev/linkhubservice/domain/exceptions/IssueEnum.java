package com.godev.linkhubservice.domain.exceptions;

public enum IssueEnum {
    ARGUMENT_NOT_VALID("Um erro aconteceu durante a validação do corpo da requisição."),
    FORMAT_REQUEST_NOT_VALID("O formato da requisição não é suportado.");

    private final String message;

    IssueEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
