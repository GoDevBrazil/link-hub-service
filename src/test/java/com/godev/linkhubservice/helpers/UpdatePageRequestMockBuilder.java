package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.UpdatePageRequest;

public class UpdatePageRequestMockBuilder {

    private final UpdatePageRequest updatePageRequest;

    public UpdatePageRequestMockBuilder() {
        this.updatePageRequest = new UpdatePageRequest();
    }

    public static UpdatePageRequestMockBuilder getBuilder() {
        return new UpdatePageRequestMockBuilder();
    }

    public UpdatePageRequestMockBuilder mock() {
        this.updatePageRequest.setSlug("Air Fryer");
        this.updatePageRequest.setTitle("Pinador profissional");
        this.updatePageRequest.setDescription("Nem tentou, nem tentou...");
        this.updatePageRequest.setPhoto("https://i.imgur.com/MFMnGqS.jpeg");
        this.updatePageRequest.setFontColor("#993399");
        this.updatePageRequest.setBackgroundType("IMAGE");
        this.updatePageRequest.setBackgroundValue("https://i.imgur.com/rel7mXd.jpeg");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullSlug() {
        this.updatePageRequest.setSlug(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidLengthSlug() {
        this.updatePageRequest.setSlug("Air");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullTitle() {
        this.updatePageRequest.setTitle(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidLengthTitle() {
        this.updatePageRequest.setTitle("nt");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullDescription() {
        this.updatePageRequest.setDescription(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidLengthDescription() {
        this.updatePageRequest.setDescription("Invalid length");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullPhoto() {
        this.updatePageRequest.setPhoto(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidFormatPhoto() {
        this.updatePageRequest.setPhoto("https://st2.depositphotos.com/1718692/8500/i/450/depositphotos_85003572-" +
                "stock-photo-fence-near-road-down-the.jpg");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullFontColor() {
        this.updatePageRequest.setFontColor(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidFormatFontColor() {
        this.updatePageRequest.setFontColor("#ab");

        return this;
    }

    public UpdatePageRequestMockBuilder withNullBackgroundType() {
        this.updatePageRequest.setBackgroundType(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withNullBackgroundValue() {
        this.updatePageRequest.setBackgroundValue(null);

        return this;
    }

    public UpdatePageRequestMockBuilder withInvalidFormatBackgroundValue() {
        this.updatePageRequest.setBackgroundValue("https://st2.depositphotos.com/1718692/8500/i/450/depositphotos_85003572-" +
                "stock-photo-fence-near-road-down-the.jpg");

        return this;
    }

    public UpdatePageRequest build() {
        return updatePageRequest;
    }
}
