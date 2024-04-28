package com.godev.linkhubservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LinkResponse {

    private Boolean status;
    private Integer linkOrder;
    private String title;
    private String href;
    private String backgroundColor;
    private String textColor;
    private String borderType;
}
