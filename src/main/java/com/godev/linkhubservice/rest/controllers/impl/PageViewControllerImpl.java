package com.godev.linkhubservice.rest.controllers.impl;

import com.godev.linkhubservice.domain.models.PageView;
import com.godev.linkhubservice.rest.controllers.PageViewController;
import com.godev.linkhubservice.services.PageViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping(value = "/pageview")
@Slf4j
public class PageViewControllerImpl implements PageViewController {

    private final PageViewService pageViewService;

    public PageViewControllerImpl(PageViewService pageViewService) {
        this.pageViewService = pageViewService;
    }

    @Override
    public ResponseEntity<String> pageViewCounter(Integer pageId) {
        log.info("Initialing PageView counter");

        this.pageViewService.pageViewCounter(pageId);

        log.info("PageView with slug {} accounted", pageId);

        return ResponseEntity.noContent().build();
    }
}
