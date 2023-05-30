package com.godev.linkhubservice.domain.constants;

public class ValidationConstants {

    private ValidationConstants() {
    }

    public static final String  EMAIL_LENGTH_ERROR = "O campo email precisa ter entre 6 e 50 caracteres.";
    public static final String  NAME_LENGTH_ERROR = "O campo nome precisa ter entre 4 e 20 caracteres.";

    public static final String  EMAIL_FORMAT_ERROR = "O campo email precisa ser preenchido com um e-mail no formato válido.";
    public static final String  PASSWORD_FORMAT_ERROR = "O campo senha precisa ter entre 8 e 16 caracteres contendo pelo menos " +
            "uma letra maíúscula, uma minúscula, um número e um caractere especial.";

    public static final String  EMAIL_REQUIRED_ERROR = "O campo email é obrigatório!";
    public static final String  PASSWORD_REQUIRED_ERROR = "O campo senha é obrigatório!";
    public static final String  NAME_REQUIRED_ERROR = "O campo nome é obrigatório!";
}
