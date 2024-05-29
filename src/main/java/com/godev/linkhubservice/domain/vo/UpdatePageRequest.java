package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_OR_URL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.URL_IMGUR_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.DESCRIPTION_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_FONT_COLOR_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_IMGUR_URL_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.URL_OR_HEX_FORMAT_ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class UpdatePageRequest {

    @Schema(name = "slug", defaultValue = "linkhub", description = "Unique identification of page.")
    @Length(min = 4, max = 50, message = SLUG_LENGTH_ERROR)
    private String slug;

    @Schema(name = "title", defaultValue = "Link Hub", description = "Title of the page.")
    @Length(min = 4, max = 100, message = TITLE_LENGTH_ERROR)
    private String title;

    @Schema(name = "description", defaultValue = "Link Hub page description.", description = "A description of the page")
    @Length(min = 20, max = 200, message = DESCRIPTION_LENGTH_ERROR)
    private String description;

    @Schema(name = "photo", defaultValue = "https://i.imgur.com/t7rv6OF.png", description = "A photo of the page")
    @Pattern(regexp = URL_IMGUR_VALIDATION_REGEX, message = INVALID_IMGUR_URL_FORMAT_ERROR)
    private String photo;

    @Schema(name = "font color", defaultValue = "#212121", description = "A photo of the page")
    @Pattern(regexp = HEX_VALIDATION_REGEX, message = INVALID_FONT_COLOR_FORMAT_ERROR)
    private String fontColor;

    @Schema(name = "background type", defaultValue = "COLOR", description = "A background type of the page")
    private String backgroundType;

    @Schema(name = "background value", defaultValue = "#F4F4F4", description = "A background color of the page")
    @Pattern(regexp = HEX_OR_URL_VALIDATION_REGEX, message = URL_OR_HEX_FORMAT_ERROR)
    private String backgroundValue;
}
