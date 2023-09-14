package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.rest.controllers.PageController;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/page")
@Slf4j
public class PageControllerImpl implements PageController {

    private final PageService pageService;

    public PageControllerImpl(PageService pageService) {
        this.pageService = pageService;
    }


    @Override
    public ResponseEntity<PageResponse> create(CreatePageRequest createPageRequest) {

        log.info("Starting create page {}", createPageRequest.getSlug());

        var pageResponse = pageService.create(createPageRequest);

        log.info("Page {} saved in database", createPageRequest.getSlug());

        return ResponseEntity.status(HttpStatus.CREATED).body(pageResponse);
    }

    @Override
    public ResponseEntity<PageResponse> update(UpdatePageRequest updatePageRequest, Integer id) {

        log.info("Starting update page");

        var pageResponse = pageService.update(updatePageRequest, id);

        log.info("Page {} updated and saved in database", pageResponse.getSlug());

        return ResponseEntity.ok(pageResponse);
    }

    @Override
    public ResponseEntity<List<PageResponse>> findPagesByAccountId() {
        var userPages = pageService.findPagesByAccountId();
        return ResponseEntity.ok().body(userPages);
    }

}
