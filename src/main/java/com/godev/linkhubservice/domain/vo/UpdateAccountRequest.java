package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.RegexConstants.EMAIL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.PASSWORD_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class UpdateAccountRequest {

    @Schema(name = "name", defaultValue = "John Doe", description = "Nome of a person or business")
    @Length(min = 4, max = 20, message = NAME_LENGTH_ERROR)
    private String name;

    @Schema(name = "email", defaultValue = "john.doe@email.com", description = "E-mail of a person or business")
    @Length(min = 6, max = 50, message = EMAIL_LENGTH_ERROR)
    @Pattern(regexp = EMAIL_VALIDATION_REGEX, message = EMAIL_FORMAT_ERROR)
    private String email;

    @Schema(name = "password", defaultValue = "Makako@123", description = "Password of a person or business")
    @Pattern(regexp = PASSWORD_VALIDATION_REGEX,
            message = PASSWORD_FORMAT_ERROR)
    private String password;

}
