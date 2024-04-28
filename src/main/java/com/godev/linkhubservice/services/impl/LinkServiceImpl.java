package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.models.Link;
import com.godev.linkhubservice.domain.repository.LinkRepository;
import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;
import com.godev.linkhubservice.services.LinkService;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final ModelMapper mapper;
    private final PageService pageService;

    public LinkServiceImpl(LinkRepository linkRepository, ModelMapper mapper, PageService pageService) {
        this.linkRepository = linkRepository;
        this.mapper = mapper;
        this.pageService = pageService;
    }

    @Override
    public LinkResponse create(LinkRequest linkRequest) {

        var page = this.pageService.findById(linkRequest.getPageId());

        var link = this.mapper.map(linkRequest, Link.class);

        link.setPageId(page);

        return null;
    }
}
