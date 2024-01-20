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

        log.info("Finding pages of this logged user");

        var userPages = pageService.findPagesByAccountId();

        log.info("Listing pages of this logged user");

        return ResponseEntity.ok().body(userPages);
    }

    @Override
    public ResponseEntity<PageResponse> findById(Integer id) {

        log.info("Finding page with id {}", id);

        var pageResponse = pageService.findById(id);

        log.info("Returning page");

        return ResponseEntity.ok(pageResponse);
    }

    @Override
    public ResponseEntity<Void> delete(Integer id) {

        log.info("Initialing delete process verifications");

        this.pageService.delete(id);

        log.info("Page with id {} deleted", id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> pageViewCounter(String slug) {

        log.info("Initialing PageView counter");

        this.pageService.pageViewCounter(slug);

        log.info("PageView with slug {} accounted", slug);

        return ResponseEntity.noContent().build();
    }

}
