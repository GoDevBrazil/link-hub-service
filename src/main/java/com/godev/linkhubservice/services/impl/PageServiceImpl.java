package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.services.AccountService;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_TYPE_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_VALUE;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_FONT_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_PHOTO;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.PAGE_BACKGROUND_TYPE_IMAGE;
import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.URL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BACKGROUND_TYPE_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR;

@Service
@Slf4j
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final AccountService accountService;

    public PageServiceImpl(PageRepository pageRepository, AccountService accountService) {
        this.pageRepository = pageRepository;
        this.accountService = accountService;
    }

    @Override
    public PageResponse create(CreatePageRequest createPageRequest) {

        log.info("Verifying if user is authenticated.");

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = accountService.findByEmail(userDetails.getUsername());

        log.info("Verifying if slug {} already exists.", createPageRequest.getSlug());

        this.pageRepository.findBySlug(createPageRequest.getSlug())
                .ifPresent(page -> {
                    throw new RuleViolationException(
                            new Issue(IssueEnum.ARGUMENT_NOT_VALID, String.format(SLUG_EXISTS_ERROR, createPageRequest.getSlug()))
                    );
                });

        log.info("Verifying if obligatory fields are missing. ");

        setDefaultValues(createPageRequest);

        log.info("Validating background type.");

        validateBackgroundType(createPageRequest);

        var page = Page
                .builder()
                .withAccount(account)
                .withSlug(createPageRequest.getSlug())
                .withTitle(createPageRequest.getTitle())
                .withDescription(createPageRequest.getDescription())
                .withPhoto(createPageRequest.getPhoto())
                .withFontColor(createPageRequest.getFontColor())
                .withBackgroundType(createPageRequest.getBackgroundType())
                .withBackgroundValue(createPageRequest.getBackgroundValue())
                .withCreatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .withUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC))
                .build();

        log.info("Starting saving page {} in database.", page.getSlug());

        var pageSaved = pageRepository.save(page);


        return PageResponse
                .builder()
                .withId(pageSaved.getId())
                .withSlug(pageSaved.getSlug())
                .withTitle(pageSaved.getTitle())
                .withDescription(pageSaved.getDescription())
                .withPhoto(pageSaved.getPhoto())
                .withFontColor(pageSaved.getFontColor())
                .withBackgroundType(pageSaved.getBackgroundType())
                .withBackgroundValue(pageSaved.getBackgroundValue())
                .withCreatedAt(pageSaved.getCreatedAt())
                .withUpdatedAt(pageSaved.getUpdatedAt())
                .build();
    }

    @Override
    public PageResponse update(UpdatePageRequest updatePageRequest, Integer id) {
        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = accountService.findByEmail(userDetails.getUsername());

        // TODO implementar findById no PageService para retornar exceção (404) caso não encontre a Page
        var page = pageRepository.findById(id);

        return null;
    }

    private void validateBackgroundType(CreatePageRequest createPageRequest) {
        if(!createPageRequest.getBackgroundType().equalsIgnoreCase(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR) &&
                !createPageRequest.getBackgroundType().equalsIgnoreCase(PAGE_BACKGROUND_TYPE_IMAGE)) {
            throw new RuleViolationException(
                    new Issue(IssueEnum.ARGUMENT_NOT_VALID, INVALID_BACKGROUND_TYPE_ERROR)
            );
        }

        if(createPageRequest.getBackgroundType().equalsIgnoreCase(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR) &&
                !createPageRequest.getBackgroundValue().matches(HEX_VALIDATION_REGEX)){
            throw new RuleViolationException(
                    new Issue(IssueEnum.ARGUMENT_NOT_VALID, INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR)
            );
        }

        if(createPageRequest.getBackgroundType().equalsIgnoreCase(PAGE_BACKGROUND_TYPE_IMAGE) &&
                !createPageRequest.getBackgroundValue().matches(URL_VALIDATION_REGEX)){
            throw new RuleViolationException(
                    new Issue(IssueEnum.ARGUMENT_NOT_VALID, INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR)
            );
        }
    }

    private void setDefaultValues(CreatePageRequest createPageRequest) {
        if(ObjectUtils.isEmpty(createPageRequest.getPhoto())){
            createPageRequest.setPhoto(DEFAULT_PAGE_PHOTO);
        }

        if(ObjectUtils.isEmpty(createPageRequest.getFontColor())){
            createPageRequest.setFontColor(DEFAULT_PAGE_FONT_COLOR);
        }

        if(ObjectUtils.isEmpty(createPageRequest.getBackgroundType())) {
            createPageRequest.setBackgroundType(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR);
        }

        if(ObjectUtils.isEmpty(createPageRequest.getBackgroundValue())){
            createPageRequest.setBackgroundValue(DEFAULT_PAGE_BACKGROUND_VALUE);
        }
    }
}
