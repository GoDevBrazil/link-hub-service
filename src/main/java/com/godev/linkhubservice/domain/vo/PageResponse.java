package com.godev.linkhubservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse {

    private Integer id;
    private String slug;
    private String title;
    private String description;
    private String photo;
    private String fontColor;
    private String backgroundType;
    private String backgroundValue;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
