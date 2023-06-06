package com.godev.linkhubservice.domain.constants;

public class RegexConstants {

    private RegexConstants() {
    }

    public static final String  EMAIL_VALIDATION_REGEX = "^[a-z0-9!#$%&'*+=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
    public static final String  PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,16}$";
    public static final String  URL_VALIDATION_REGEX = "^(?:https?:)\\/\\/i\\.imgur\\.com\\/[\\w-]+\\.(?:jpg|jpeg|png|gif|bmp)$";

}
