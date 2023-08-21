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

    public UpdatePageRequest build() {
        return updatePageRequest;
    }
}
