package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class ForbiddenException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public ForbiddenException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
