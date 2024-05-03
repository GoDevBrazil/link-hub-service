package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.HREF_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.HREF_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_FONT_COLOR_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_ORDER_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_TITLE_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PAGE_ID_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class LinkRequest {

    @Schema(name = "status", defaultValue = "false", description = "Enable or disable a link")
    private Boolean status;

    @Schema(name = "link order", defaultValue = "0", description = "Arranges the order of links")
    @Max(value = 999999, message = LINK_ORDER_LENGTH_ERROR)
    private Integer linkOrder;

    @Schema(name = "title", defaultValue = "The best link ever", description = "Link title")
    @Length(min = 4, max = 100, message = TITLE_LENGTH_ERROR)
    @NotBlank(message = LINK_TITLE_REQUIRED_ERROR)
    private String title;

    @Schema(name = "href", defaultValue = "https://www.google.com.br/logos/google.jpg", description = "A link")
    @Length(min = 7, max = 100, message = HREF_LENGTH_ERROR)
    @NotBlank(message = HREF_REQUIRED_ERROR)
    private String href;

    @Schema(name = "background color", defaultValue = "#cacaca", description = "A background color")
    @Pattern(regexp = HEX_VALIDATION_REGEX, message = INVALID_FONT_COLOR_FORMAT_ERROR)
    private String backgroundColor;

    @Schema(name = "text color", defaultValue = "#212121", description = "A text color")
    @Pattern(regexp = HEX_VALIDATION_REGEX, message = INVALID_FONT_COLOR_FORMAT_ERROR)
    private String textColor;

    @Schema(name = "border type", defaultValue = "square",description = "A border type")
    private String borderType;

    @Schema(name = "pageId", defaultValue = "1", description = "Unique identification of page.")
    @NotNull(message = PAGE_ID_REQUIRED_ERROR)
    private Integer pageId;
}
