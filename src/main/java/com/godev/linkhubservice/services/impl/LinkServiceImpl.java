package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Link;
import com.godev.linkhubservice.domain.repository.LinkRepository;
import com.godev.linkhubservice.domain.vo.LinkRequest;
import com.godev.linkhubservice.domain.vo.LinkResponse;
import com.godev.linkhubservice.services.LinkService;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_LINK_BORDER_TYPE_SQUARE;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.LINK_BORDER_TYPE_ROUNDED;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BORDER_TYPE_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.ARGUMENT_NOT_VALID;

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

        log.info("Validating link fields");

        this.settingStatusDefaultValue(linkRequest);

        this.validatingBorderType(linkRequest);

        var link = this.mapper.map(linkRequest, Link.class);

        var pageLinks = this.linkRepository.findLinksByPageId(link.getPageId());

        this.settingLinkOrder(pageLinks, link);

        log.info("Starting saving link {} in database", link.getHref());

        var linkSaved = this.linkRepository.save(link);

        return this.mapper.map(linkSaved, LinkResponse.class);
    }

    private void settingStatusDefaultValue(LinkRequest linkRequest) {
        if (linkRequest.getStatus() == null) {
            linkRequest.setStatus(false);
        }
    }

    private void validatingBorderType(LinkRequest linkRequest) {
        if (!linkRequest.getBorderType().equalsIgnoreCase(DEFAULT_LINK_BORDER_TYPE_SQUARE) &&
        !linkRequest.getBorderType().equalsIgnoreCase(LINK_BORDER_TYPE_ROUNDED)) {
            throw new RuleViolationException(
                    new Issue(ARGUMENT_NOT_VALID, INVALID_BORDER_TYPE_ERROR)
            );
        }
    }

    private void settingLinkOrder(List<Link> pageLinks, Link link) {
        if (pageLinks.isEmpty()) {
            link.setLinkOrder(0);
        } else {
            link.setLinkOrder(pageLinks.size());
        }
    }
}
