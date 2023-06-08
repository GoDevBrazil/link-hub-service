package com.godev.linkhubservice.domain.constants;

public class IssueDetails {

    private IssueDetails() {
    }

    public static final String EMAIL_EXISTS_ERROR = "O e-mail %s já está cadastrado.";
    public static final String SLUG_EXISTS_ERROR = "O perfil %s já está cadastrado.";
    public static final String GENERATE_AUTH_TOKEN_ERROR = "Um erro ocorreu ao gerar o token de autenticação.";
    public static final String  INVALID_CREDENTIALS_ERROR = "Usuário e/ou senha inválidos.";
    public static final String  INVALID_URL_ERROR = "O formato esperado de url de imagem é da plataforma Imgur. Extensões suportadas: jpg|jpeg|png|gif|bmp.";
    public static final String  INVALID_FONT_COLOR_ERROR = "O formato da cor da fonte precisa ser em hexadecimal (#F4F4F4).";
    public static final String  INVALID_BACKGROUND_TYPE_ERROR = "O tipo esperado de cor de fundo é COLOR ou IMAGE.";
    public static final String  INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR = "O valor esperado para o fundo é em hexadecimal (#F4F4F4).";
    public static final String  INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR = "O valor esperado para o fundo deve seguir o padrão de url de imagem da plataforma Imgur.";
    public static final String  EMAIL_NOT_FOUND_ERROR = "Conta com o email '%s' não encontrada.";

}
