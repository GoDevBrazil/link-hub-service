package com.godev.linkhubservice.rest.exceptions;

import com.godev.linkhubservice.domain.exceptions.BadRequestException;
import com.godev.linkhubservice.domain.exceptions.InvalidJwtException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Issue handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return new Issue(IssueEnum.ARGUMENT_NOT_VALID,
                e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Issue handlerHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return new Issue(IssueEnum.ARGUMENT_NOT_VALID, e.getMessage().split(":")[0]);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Issue handlerHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e){
        return new Issue(IssueEnum.FORMAT_REQUEST_NOT_VALID, e.getMessage());
    }

    @ExceptionHandler(RuleViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Issue handlerRuleViolationException(RuleViolationException e){
        return e.getIssue();
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Issue handlerObjectNotFoundException(ObjectNotFoundException e){
        return e.getIssue();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Issue handlerBadRequestException(BadRequestException e) {
        return e.getIssue();
    }

    @ExceptionHandler(InvalidJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Issue handlerInvalidJwtException(InvalidJwtException e) {
        return e.getIssue();
    }
}
