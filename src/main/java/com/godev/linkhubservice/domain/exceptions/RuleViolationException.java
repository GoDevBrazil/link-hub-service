package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class RuleViolationException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public RuleViolationException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
