package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.PAGE_BACKGROUND_TYPE_IMAGE;

public class CreatePageRequestMockBuilder {

    private final CreatePageRequest createPageRequest;

    public CreatePageRequestMockBuilder() {
        this.createPageRequest = new CreatePageRequest();
    }

    public static CreatePageRequestMockBuilder getBuilder() {
        return new CreatePageRequestMockBuilder();
    }

    public CreatePageRequestMockBuilder mock() {
        this.createPageRequest.setSlug("Jean Law");
        this.createPageRequest.setTitle("O melhor tutor de tecnologia!");
        this.createPageRequest.setDescription("Ajuda os amigos de coração.");
        this.createPageRequest.setPhoto("https://i.imgur.com/36bRmSn.jpeg");
        this.createPageRequest.setFontColor("#0047AB");
        this.createPageRequest.setBackgroundType("COLOR");
        this.createPageRequest.setBackgroundValue("#193153");

        return this;
    }

    public CreatePageRequestMockBuilder withNullSlug() {

        this.createPageRequest.setSlug(null);

        return this;
    }

    public CreatePageRequestMockBuilder withEmptySlug() {

        this.createPageRequest.setSlug("");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidLengthSlug() {

        this.createPageRequest.setSlug("Law");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidLengthTittle() {

        this.createPageRequest.setTitle("Law");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidLengthDescription() {

        this.createPageRequest.setDescription("O law é legal.");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidUrlFormatPhoto() {

        this.createPageRequest.setPhoto("https://img.elo7.com.br/product/original/3254FDB/" +
                "bob-esponja-e-patrick-em-camadas-arquivo-de-corte-personalizados-bob-esponja-e-patrick.jpg");

        return this;
    }

    public CreatePageRequestMockBuilder withValidUrlBackgroundValue() {

        this.createPageRequest.setBackgroundValue("https://i.imgur.com/36bRmSn.jpeg");

        return this;
    }
    public CreatePageRequestMockBuilder withNullPhoto() {

        this.createPageRequest.setPhoto(null);

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidLengthFontColor() {

        this.createPageRequest.setFontColor("#ab");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidRgbFormatFontColor() {

        this.createPageRequest.setFontColor("(54, 54, 54)");

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidNameFormatFontColor() {

        this.createPageRequest.setFontColor("deep purple");

        return this;
    }

    public CreatePageRequestMockBuilder withNullFontColor() {

        this.createPageRequest.setFontColor(null);

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidRgbFormatBackgroundValue() {

        this.createPageRequest.setBackgroundValue("(54, 54, 54)");

        return this;
    }

    public CreatePageRequestMockBuilder withNullBackgroundType() {

        this.createPageRequest.setBackgroundType(null);

        return this;
    }

    public CreatePageRequestMockBuilder withInvalidBackgroundType() {

        this.createPageRequest.setBackgroundType("Javax");

        return this;
    }
    public CreatePageRequestMockBuilder withBackgroundTypeImage() {

        this.createPageRequest.setBackgroundType(PAGE_BACKGROUND_TYPE_IMAGE);

        return this;
    }

    public CreatePageRequestMockBuilder withNullBackgroundValue() {

        this.createPageRequest.setBackgroundValue(null);

        return this;
    }

    public CreatePageRequest build() { return createPageRequest;}
}
