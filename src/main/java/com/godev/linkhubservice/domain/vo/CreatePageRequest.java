package com.godev.linkhubservice.domain.vo;


import com.godev.linkhubservice.domain.enums.BackgroundType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import static com.godev.linkhubservice.domain.constants.ValidationConstants.BACKGROUND_VALUE_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.DESCRIPTION_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.FONT_COLOR_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.PHOTO_LENGTH_ERROR;
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

    @Schema(name = "photo", defaultValue = "Link hub page photo.", description = "A photo of the page")
    @Length(min = 20, max = 100, message = PHOTO_LENGTH_ERROR)
    private String photo;

    @Schema(name = "font color", defaultValue = "#212121", description = "A photo of the page")
    @Length(min = 2, max = 7, message = FONT_COLOR_LENGTH_ERROR)
    private String fontColor;

    @Schema(name = "background type", defaultValue = "color", description = "A background type of the page")
    @Nullable
    private BackgroundType backgroundType;

    @Schema(name = "background value", defaultValue = "#F4F4F4", description = "A background color of the page")
    @Length(min = 2, max = 50, message = BACKGROUND_VALUE_LENGTH_ERROR)
    private String backgroundValue;
}
