package com.godev.linkhubservice.domain.constants;

public class IssueDetails {

    private IssueDetails() {
    }

    public static final String EMAIL_EXISTS_ERROR = "O e-mail %s já está cadastrado!";
    public static final String GENERATE_AUTH_TOKEN_ERROR = "Um erro ocorreu ao gerar o token de autenticação";
    public static final String  INVALID_CREDENTIALS_ERROR = "Usuário e/ou senha inválidos.";
    public static final String  EMAIL_NOT_FOUND_ERROR = "Conta com o email '%s' não encontrada";
}
