package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

public class ObjectNotFoundException extends RuntimeException {

    @Getter
    private final transient Issue issue;

    public ObjectNotFoundException(Issue issue) {
        super(issue.getMessage());
        this.issue = issue;
    }
}
