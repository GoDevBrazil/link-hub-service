package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;
import com.godev.linkhubservice.rest.controllers.LinkController;
import com.godev.linkhubservice.services.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/link")
@Slf4j
public class LinkControllerImpl implements LinkController {

    private final LinkService linkService;

    public LinkControllerImpl(LinkService linkService) { this.linkService = linkService; }

    @Override
    public ResponseEntity<LinkResponse> create(LinkRequest linkRequest) {

        var linkResponse = linkService.create(linkRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(linkResponse);
    }
}
