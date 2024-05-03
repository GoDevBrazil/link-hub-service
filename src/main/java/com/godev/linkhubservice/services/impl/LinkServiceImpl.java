package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.models.Link;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.LinkRepository;
import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;
import com.godev.linkhubservice.services.LinkService;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

        this.pageService.findById(linkRequest.getPageId());

        this.mapper.typeMap(LinkRequest.class,Link.class).addMappings(mp -> {
            mp.skip(Link::setId);
        });

        if (linkRequest.getStatus() == null) {
            linkRequest.setStatus(false);
        }

        var link = this.mapper.map(linkRequest, Link.class);

        var pageLinks = this.linkRepository.findLinksByPageId(link.getPageId());

        if (pageLinks.isEmpty()) {
            link.setLinkOrder(0);
        } else {
            link.setLinkOrder(pageLinks.size());
        }

        var linkSaved = this.linkRepository.save(link);

        return this.mapper.map(linkSaved, LinkResponse.class);
    }
}
