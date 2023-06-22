package com.godev.linkhubservice.domain.constants;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final String  EMAIL_LENGTH_ERROR = "O campo email precisa ter entre 6 e 50 caracteres.";
    public static final String  NAME_LENGTH_ERROR = "O campo nome precisa ter entre 4 e 20 caracteres.";
    public static final String  SLUG_LENGTH_ERROR = "O campo slug precisa ter entre 4 e 50 caracteres.";
    public static final String  TITLE_LENGTH_ERROR = "O campo título precisa ter entre 4 e 100 caracteres.";
    public static final String  DESCRIPTION_LENGTH_ERROR = "O campo descrição precisa ter entre 20 e 200 caracteres.";

    public static final String  EMAIL_FORMAT_ERROR = "O campo email precisa ser preenchido com um e-mail no formato válido.";
    public static final String  URL_OR_HEX_FORMAT_ERROR = "O campo valor de fundo precisa seguir o padrão da plataforma Imgur " +
            "ou precisa ser no formato de hexadecimal (#F4F4F4).";
    public static final String  PASSWORD_FORMAT_ERROR = "O campo senha precisa ter entre 8 e 16 caracteres contendo pelo menos " +
            "uma letra maíúscula, uma minúscula, um número e um caractere especial.";
    public static final String INVALID_URL_FORMAT_ERROR = "O formato esperado de url de imagem é da plataforma Imgur. " +
            "Extensões suportadas: jpg|jpeg|png|gif|bmp.";
    public static final String INVALID_FONT_COLOR_FORMAT_ERROR = "O formato esperado de cor da fonte é em hexadecimal (#F4F4F4) " +
            "e precisa ter entre 4 e 7 caracteres.";

    public static final String  INVALID_BACKGROUND_TYPE_ERROR = "O tipo esperado de cor de fundo é COLOR ou IMAGE.";
    public static final String  INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR = "O valor esperado para o fundo é em hexadecimal (#F4F4F4).";
    public static final String  INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR = "O valor esperado para o fundo deve seguir " +
            "o padrão de url de imagem da plataforma Imgur.";

    public static final String  EMAIL_REQUIRED_ERROR = "O campo email é obrigatório!";
    public static final String  PASSWORD_REQUIRED_ERROR = "O campo senha é obrigatório!";
    public static final String  NAME_REQUIRED_ERROR = "O campo nome é obrigatório!";
    public static final String  SLUG_REQUIRED_ERROR = "O campo slug é obrigatório!";
}
