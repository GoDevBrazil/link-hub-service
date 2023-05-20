package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class InvalidJwtException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public InvalidJwtException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
