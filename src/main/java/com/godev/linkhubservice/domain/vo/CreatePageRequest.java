package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.RegexConstants.EMAIL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.PASSWORD_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.DESCRIPTION_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.EMAIL_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.NAME_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.NAME_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PASSWORD_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PASSWORD_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class CreatePageRequest {

    @Schema(name = "slug", defaultValue = "linkhub", description = "Unique identification of page.")
    @Length(min = 4, max = 50, message = SLUG_LENGTH_ERROR)
    @NotBlank(message = SLUG_REQUIRED_ERROR)
    private String slug;

    @Schema(name = "title", defaultValue = "Link Hub", description = "Title of the page.")
    @Length(min = 4, max = 100, message = TITLE_LENGTH_ERROR)
    private String title;

    @Schema(name = "description", defaultValue = "Link hub page description.", description = "A description of the page")
    @Length(min = 20, max = 200, message = DESCRIPTION_LENGTH_ERROR)
    private String description;

    private String photo;
    private String fontColor;
    private String backgroundType;
    private String backgroundValue;

    @Schema(name = "name", defaultValue = "John Doe", description = "Nome of a person or business")
    @Length(min = 4, max = 20, message = NAME_LENGTH_ERROR)
    @NotBlank(message = NAME_REQUIRED_ERROR)
    private String name;

    @Schema(name = "email", defaultValue = "john.doe@email.com", description = "E-mail of a person or business")
    @Length(min = 6, max = 50, message = EMAIL_LENGTH_ERROR)
    @NotBlank(message = EMAIL_REQUIRED_ERROR)
    @Pattern(regexp = EMAIL_VALIDATION_REGEX, message = EMAIL_FORMAT_ERROR)
    private String email;

    @Schema(name = "password", defaultValue = "Makako@123", description = "Password of a person or business")
    @NotBlank(message = PASSWORD_REQUIRED_ERROR)
    @Pattern(regexp = PASSWORD_VALIDATION_REGEX,
            message = PASSWORD_FORMAT_ERROR)
    private String password;

}
