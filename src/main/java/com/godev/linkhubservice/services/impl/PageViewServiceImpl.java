package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.models.PageView;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.repository.PageViewRepository;
import com.godev.linkhubservice.services.PageViewService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class PageViewServiceImpl implements PageViewService {

    private final PageRepository pageRepository;
    private final PageViewRepository pageViewRepository;
    private final ModelMapper mapper;

    public PageViewServiceImpl(PageRepository pageRepository, PageViewRepository pageViewRepository, ModelMapper mapper) {
        this.pageRepository = pageRepository;
        this.pageViewRepository = pageViewRepository;
        this.mapper = mapper;
    }


    @Override
    public String pageViewCounter(Integer pageId) {
//        this.pageRepository.findById(pageId);
//
//        var pageView =
//                PageView
//                .builder()
//                .withDate(OffsetDateTime.now(ZoneOffset.UTC))
//                .withTotal(1)
//                .withPageId(pageId)
//                .build();
//
//        this.pageViewRepository.save(pageView);
        return "This endpoint is ok";
    }
}
