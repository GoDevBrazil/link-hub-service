package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.rest.controllers.PageController;
import com.godev.linkhubservice.services.PageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/page")
public class PageControllerImpl implements PageController {

    private final PageService pageService;

    public PageControllerImpl(PageService pageService) {
        this.pageService = pageService;
    }


    @Override
    public ResponseEntity<PageResponse> create(CreatePageRequest createPageRequest) {

        var pageResponse = pageService.create(createPageRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pageResponse);
    }
}
