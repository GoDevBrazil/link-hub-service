package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class ForbidenException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public ForbidenException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
