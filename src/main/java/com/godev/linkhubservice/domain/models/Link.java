package com.godev.linkhubservice.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Entity(name = "links")
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false, length = 6)
    private Integer linkOrder;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String href;

    @Column(nullable = false, length = 7)
    private String backgroundColor;

    @Column(nullable = false, length = 7)
    private String textColor;

    @Column(nullable = false, length = 8)
    private String borderType;

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private Page pageId;
}
