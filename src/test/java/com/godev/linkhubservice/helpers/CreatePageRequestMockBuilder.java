package com.godev.linkhubservice.helpers;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;

public class CreatePageRequestMockBuilder {

    private final CreatePageRequest createPageRequest;

    public CreatePageRequestMockBuilder() { this.createPageRequest = new CreatePageRequest(); }

    public static CreatePageRequestMockBuilder getBuilder(){ return new CreatePageRequestMockBuilder();}

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

    public CreatePageRequest build() { return createPageRequest;}
}
