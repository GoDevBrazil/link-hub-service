package com.godev.linkhubservice.domain.constants;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final String  EMAIL_LENGTH_ERROR = "O campo email precisa ter entre 6 e 50 caracteres.";
    public static final String  NAME_LENGTH_ERROR = "O campo nome precisa ter entre 4 e 20 caracteres.";
    public static final String  SLUG_LENGTH_ERROR = "O campo slug precisa ter entre 4 e 50 caracteres.";
    public static final String  TITLE_LENGTH_ERROR = "O campo título precisa ter entre 4 e 100 caracteres.";
    public static final String  DESCRIPTION_LENGTH_ERROR = "O campo descrição precisa ter entre 20 e 200 caracteres.";
    public static final String  PHOTO_LENGTH_ERROR = "O campo foto precisa ter entre 20 e 100 caracteres.";
    public static final String  FONT_COLOR_LENGTH_ERROR = "O campo cor da fonte precisa ter entre 4 e 7 caracteres.";
    public static final String  BACKGROUND_TYPE_LENGTH_ERROR = "O campo tipo de fundo precisa ter entre 2 e 5 caracteres.";
    public static final String  BACKGROUND_VALUE_LENGTH_ERROR = "O campo valor de fundo precisa ter entre 2 e 50 caracteres.";


    public static final String  EMAIL_FORMAT_ERROR = "O campo email precisa ser preenchido com um e-mail no formato válido.";
    public static final String  PASSWORD_FORMAT_ERROR = "O campo senha precisa ter entre 8 e 16 caracteres contendo pelo menos " +
            "uma letra maíúscula, uma minúscula, um número e um caractere especial.";

    public static final String  EMAIL_REQUIRED_ERROR = "O campo email é obrigatório!";
    public static final String  PASSWORD_REQUIRED_ERROR = "O campo senha é obrigatório!";
    public static final String  NAME_REQUIRED_ERROR = "O campo nome é obrigatório!";
    public static final String  SLUG_REQUIRED_ERROR = "O campo slug é obrigatório!";
}
