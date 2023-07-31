package com.godev.linkhubservice.domain.exceptions;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
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
