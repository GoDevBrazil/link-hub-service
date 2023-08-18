package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
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
import static com.godev.linkhubservice.domain.constants.IssueDetails.ID_NOT_FOUND_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.URL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BACKGROUND_TYPE_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.OBJECT_NOT_FOUND;

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

        var page = findById(id);

        validateSlug(updatePageRequest, page);

        page.setSlug(updatePageRequest.getSlug());
        page.setTitle(updatePageRequest.getTitle());
        page.setDescription(updatePageRequest.getDescription());
        page.setPhoto(updatePageRequest.getPhoto());
        page.setFontColor(updatePageRequest.getFontColor());
        page.setBackgroundType(updatePageRequest.getBackgroundType());
        page.setBackgroundValue(updatePageRequest.getBackgroundValue());
        page.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));


        var pageUpdated = pageRepository.save(page);

        return  PageResponse
                .builder()
                .withId(pageUpdated.getId())
                .withSlug(pageUpdated.getSlug())
                .withTitle(pageUpdated.getTitle())
                .withDescription(pageUpdated.getDescription())
                .withPhoto(pageUpdated.getPhoto())
                .withFontColor(pageUpdated.getFontColor())
                .withBackgroundType(pageUpdated.getBackgroundType())
                .withBackgroundValue(pageUpdated.getBackgroundValue())
                .withUpdatedAt(pageUpdated.getUpdatedAt())
                .build();
    }

    private void validateSlug(UpdatePageRequest updatePageRequest, Page page) {
        if(ObjectUtils.isEmpty(updatePageRequest.getSlug())){
            updatePageRequest.setSlug(page.getSlug());
        }

        if(!updatePageRequest.getSlug().equalsIgnoreCase(page.getSlug())  && slugExists(updatePageRequest)){
            throw new RuleViolationException(
                            new Issue(IssueEnum.ARGUMENT_NOT_VALID, String.format(SLUG_EXISTS_ERROR, updatePageRequest.getSlug())));
        }
    }

    @Override
    public Page findById(Integer id) {

        return this.pageRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        new Issue(OBJECT_NOT_FOUND, String.format(ID_NOT_FOUND_ERROR, id))
                ));
    }

    private boolean slugExists(UpdatePageRequest updatePageRequest){
        var slug = pageRepository.findBySlug(updatePageRequest.getSlug()).orElse(null);
        return ObjectUtils.isNotEmpty(slug);
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
