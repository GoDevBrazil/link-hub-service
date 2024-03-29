package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.models.Page;

import java.time.OffsetDateTime;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_TYPE_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_VALUE;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_FONT_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_PHOTO;

public class PageMockBuilder {

    private final Page page;

    public PageMockBuilder() {
        this.page = new Page();
    }

    public static PageMockBuilder getBuilder() {
        return new PageMockBuilder();
    }

    public PageMockBuilder mock() {

        this.page.setSlug("Jean Law");
        this.page.setTitle("O melhor tutor de tecnologia!");
        this.page.setDescription("Ajuda os amigos de coração.");
        this.page.setPhoto("https://i.imgur.com/36bRmSn.jpeg");
        this.page.setFontColor("#0047AB");
        this.page.setBackgroundType("COLOR");
        this.page.setBackgroundValue("#193153");
        this.page.setCreatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        this.page.setUpdatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        var account = new Account();
        account.setId(1);
        account.setName("Kibe");
        account.setEmail("kibe@email.com");
        account.setPassword("Kibe@1234");
        account.setCreatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        account.setUpdatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        this.page.setAccount(account);

        return this;
    }

    public PageMockBuilder withId() {

        this.page.setId(1);

        return this;
    }
    public PageMockBuilder withAccountId(Integer id) {
        var account = new Account();
        account.setId(id);
        account.setName("Kibe");
        account.setEmail("kibe@email.com");
        account.setPassword("Kibe@1234");
        account.setCreatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        account.setUpdatedAt(OffsetDateTime.parse("2023-06-09T15:20:00Z"));
        this.page.setAccount(account);

        return this;
    }
    public PageMockBuilder withDefaultPhoto() {

        this.page.setPhoto(DEFAULT_PAGE_PHOTO);

        return this;
    }
    public PageMockBuilder withDefaultFontColor() {

        this.page.setFontColor(DEFAULT_PAGE_FONT_COLOR);

        return this;
    }
    public PageMockBuilder withDefaultBackgroundTypeColor() {

        this.page.setFontColor(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR);

        return this;
    }
    public PageMockBuilder withDefaultBackgroundValue() {

        this.page.setBackgroundValue(DEFAULT_PAGE_BACKGROUND_VALUE);

        return this;
    }

    public Page build() { return page;}

}
