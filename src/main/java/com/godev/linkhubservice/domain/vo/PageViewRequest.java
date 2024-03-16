package com.godev.linkhubservice.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.godev.linkhubservice.domain.constants.ValidationConstants.PAGE_ID_REQUIRED_ERROR;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class PageViewRequest {

    @Schema(name = "pageId", defaultValue = "1", description = "Unique identification of page.")
    @NotBlank(message = PAGE_ID_REQUIRED_ERROR)
    private Integer pageId;

}
