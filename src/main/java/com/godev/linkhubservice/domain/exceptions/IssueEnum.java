package com.godev.linkhubservice.domain.exceptions;

public enum IssueEnum {
    ARGUMENT_NOT_VALID("Um erro aconteceu durante a validação do corpo da requisição."),
    FORMAT_REQUEST_NOT_VALID("O formato da requisição não é suportado."),
    OBJECT_NOT_FOUND("O objeto da requisição não pode ser encontrado no banco de dados."),
    HEADER_REQUIRED_ERROR("Um erro ocorreu durante a validação dos headers obrigatórios"),
    AUTHENTICATION_ERROR("Um erro ocorreu durante a autenticação do usuário"),
    FORBIDEN("Um erro ocorreu durante a validação de autorizações")
    ;

    private final String message;

    IssueEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
