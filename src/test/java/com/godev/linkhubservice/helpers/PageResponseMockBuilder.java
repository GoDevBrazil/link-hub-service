package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.PageResponse;

import java.time.OffsetDateTime;

public class PageResponseMockBuilder {

    private final PageResponse pageResponse;

    public PageResponseMockBuilder() {
        this.pageResponse = new PageResponse();
    }

    public static PageResponseMockBuilder getBuilder() { return new PageResponseMockBuilder();}

    public PageResponseMockBuilder mock() {

        this.pageResponse.setId(1);
        this.pageResponse.setSlug("Jean Law");
        this.pageResponse.setTitle("O melhor tutor de tecnologia!");
        this.pageResponse.setDescription("Ajuda os amigos de coração.");
        this.pageResponse.setPhoto("https://i.imgur.com/36bRmSn.jpeg");
        this.pageResponse.setFontColor("#0047AB");
        this.pageResponse.setBackgroundType("COLOR");
        this.pageResponse.setBackgroundValue("#193153");
        this.pageResponse.setCreatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        this.pageResponse.setUpdatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        var account = AccountMockBuilder.getBuilder().mock().build();
        this.pageResponse.setAccount(account);

        return this;
    }

    public PageResponse build() { return pageResponse;}
}
