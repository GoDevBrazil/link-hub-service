package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class BadRequestException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public BadRequestException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
