package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Issue {

    private final String message;
    private final List<String> details;

    public Issue(IssueEnum issueEnum, List<String> details) {
        this.message = issueEnum.getMessage();
        this.details = details;
    }

    public Issue(IssueEnum issueEnum, String details) {
        this.message = issueEnum.getMessage();
        this.details = List.of(details);
    }
}
