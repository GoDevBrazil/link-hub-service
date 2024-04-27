package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.ValidationConstants.HREF_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.HREF_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_ORDER_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_ORDER_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_STATUS_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.LINK_TITLE_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class LinkRequest {

    @Schema(name = "status", defaultValue = "false", description = "Enable or disable a link")
    @NotBlank(message = LINK_STATUS_REQUIRED_ERROR)
    private Boolean status;

    @Schema(name = "link order", defaultValue = "0", description = "Arranges the order of links")
    @Length(min = 1, max = 6, message = LINK_ORDER_LENGTH_ERROR)
    @NotNull(message = LINK_ORDER_REQUIRED_ERROR)
    private Integer linkOrder;

    @Schema(name = "title", defaultValue = "The best link ever", description = "Link title")
    @Length(min = 4, max = 100, message = TITLE_LENGTH_ERROR)
    @NotBlank(message = LINK_TITLE_REQUIRED_ERROR)
    private String title;

    @Schema(name = "href", defaultValue = "https://www.google.com.br/logos/google.jpg", description = "A link")
    @Length(min = 30, max = 100, message = HREF_LENGTH_ERROR)
    @NotBlank(message = HREF_REQUIRED_ERROR)
    private String href;

    private String backgroundColor;
    private String textColor;
    private String borderType;
}
