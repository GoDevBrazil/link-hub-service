package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.ForbiddenException;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.models.PageView;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.repository.PageViewRepository;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.PageViewRequest;
import com.godev.linkhubservice.domain.vo.PageViewResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.services.AccountService;
import com.godev.linkhubservice.services.PageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_TYPE_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_VALUE;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_FONT_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_PHOTO;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.PAGE_BACKGROUND_TYPE_IMAGE;
import static com.godev.linkhubservice.domain.constants.IssueDetails.ID_NOT_FOUND_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.USER_NOT_ALLOWED;
import static com.godev.linkhubservice.domain.constants.RegexConstants.HEX_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.RegexConstants.URL_VALIDATION_REGEX;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BACKGROUND_TYPE_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.ARGUMENT_NOT_VALID;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.FORBIDDEN;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.OBJECT_NOT_FOUND;

@Service
@Slf4j
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final AccountService accountService;
    private final ModelMapper mapper;
    private final PageViewRepository pageViewRepository;

    public PageServiceImpl(PageRepository pageRepository, AccountService accountService, ModelMapper mapper, PageViewRepository pageViewRepository) {
        this.pageRepository = pageRepository;
        this.accountService = accountService;
        this.mapper = mapper;
        this.pageViewRepository = pageViewRepository;
    }

    @Override
    public PageResponse create(CreatePageRequest createPageRequest) {

        log.info("Verifying if user is authenticated.");

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = this.accountService.findByEmail(userDetails.getUsername());

        log.info("Verifying if slug {} already exists.", createPageRequest.getSlug());

        this.pageRepository.findBySlug(createPageRequest.getSlug())
                .ifPresent(page -> {
                    throw new RuleViolationException(
                            new Issue(ARGUMENT_NOT_VALID, String.format(SLUG_EXISTS_ERROR, createPageRequest.getSlug()))
                    );
                });

        log.info("Verifying if obligatory fields are missing. ");

        this.setDefaultValues(createPageRequest);

        log.info("Validating background type.");

        this.validateBackgroundType(createPageRequest);

        var page = this.mapper.map(createPageRequest, Page.class);

        page.setAccount(account);
        page.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        page.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        log.info("Starting saving page {} in database.", page.getSlug());

        var pageSaved = this.pageRepository.save(page);

        return this.mapper.map(pageSaved, PageResponse.class);
    }

    @Override
    public PageResponse update(UpdatePageRequest updatePageRequest, Integer id) {
        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = this.accountService.findByEmail(userDetails.getUsername());

        var page = this.findPageById(id);

        this.validateAuthorizations(account, page);

        this.validateSlug(updatePageRequest, page);

        this.setEmptyFields(updatePageRequest, page);

        this.mapper.map(updatePageRequest, page);

        page.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        log.info("Updating page {}", page.getSlug());

        var pageUpdated = this.pageRepository.save(page);

        return  this.mapper.map(pageUpdated, PageResponse.class);
    }

    @Override
    public PageResponse findById(Integer id) {

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = this.accountService.findByEmail(userDetails.getUsername());

        var page = this.findPageById(id);

        this.validateAuthorizations(account, page);

        return this.mapper.map(page, PageResponse.class);
    }

    @Override
    public List<PageResponse> findPagesByAccountId() {
        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = this.accountService.findByEmail(userDetails.getUsername());

        log.info("Getting pages of account id {} ", account.getId());

        var pageList = this.pageRepository.findPagesByAccount_Id(account.getId());

        return pageList.stream().map(page -> this.mapper.map(page, PageResponse.class)).toList();
    }

    @Override
    public void delete(Integer id) {

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = this.accountService.findByEmail(userDetails.getUsername());

        var page = this.findPageById(id);

        this.validateAuthorizations(account, page);

        log.info("Deletion process in progress");

        this.pageRepository.delete(page);
    }

    @Override
    public PageViewResponse pageViewCounter(PageViewRequest pageViewRequest) {

        var page = this.findPageById(pageViewRequest.getPageId());

        var date = LocalDate.now().atTime(0, 0).atOffset(ZoneOffset.ofHours(0));

        var pageView = this.pageViewRepository.findByPageIdAndDate(page, date).orElse(null);

        if(ObjectUtils.isEmpty(pageView)){
            pageView = PageView.builder()
                    .withTotal(1)
                    .withDate(date)
                    .withPageId(page)
                    .build();
        }else {
            pageView.setTotal(pageView.getTotal()+1);
        }

        var pageViewSaved = this.pageViewRepository.save(pageView);

        return this.mapper.map(pageViewSaved, PageViewResponse.class);
    }

    private void validateAuthorizations(Account account, Page page) {
        log.info("Verifying user authorization to edit page with id {}", page.getId());

        if(!Objects.equals(account.getId(), page.getAccount().getId())){
            throw new ForbiddenException(
                    new Issue(FORBIDDEN, String.format(USER_NOT_ALLOWED, page.getId()))
            );
        }
    }

    private Page findPageById(Integer id) {

        log.info("Searching this page in database");

        return this.pageRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        new Issue(OBJECT_NOT_FOUND, String.format(ID_NOT_FOUND_ERROR, id))
                ));
    }

    private  void setEmptyFields(UpdatePageRequest updatePageRequest, Page page) {
        log.info("Verifying and setting empty fields");

        if(ObjectUtils.isEmpty(updatePageRequest.getTitle())){
            updatePageRequest.setTitle(page.getTitle());
        }

        if(ObjectUtils.isEmpty(updatePageRequest.getDescription())){
            updatePageRequest.setDescription(page.getDescription());
        }

        if(ObjectUtils.isEmpty(updatePageRequest.getPhoto())){
            updatePageRequest.setPhoto(page.getPhoto());
        }

        if(ObjectUtils.isEmpty(updatePageRequest.getFontColor())){
            updatePageRequest.setFontColor(page.getFontColor());
        }

        if(ObjectUtils.isEmpty(updatePageRequest.getBackgroundType())){
            updatePageRequest.setBackgroundType(page.getBackgroundType());
        }

        if(ObjectUtils.isEmpty(updatePageRequest.getBackgroundValue())){
            updatePageRequest.setBackgroundValue(page.getBackgroundValue());
        }
    }

    private void validateSlug(UpdatePageRequest updatePageRequest, Page page) {
        log.info("Validating slug {}", updatePageRequest.getSlug());

        if(ObjectUtils.isEmpty(updatePageRequest.getSlug())){
            updatePageRequest.setSlug(page.getSlug());
        }

        if(!updatePageRequest.getSlug().equalsIgnoreCase(page.getSlug())  && this.slugExists(updatePageRequest)){
            throw new RuleViolationException(
                    new Issue(ARGUMENT_NOT_VALID, String.format(SLUG_EXISTS_ERROR, updatePageRequest.getSlug())));
        }
    }

    private boolean slugExists(UpdatePageRequest updatePageRequest){
        var slug = this.pageRepository.findBySlug(updatePageRequest.getSlug()).orElse(null);
        return ObjectUtils.isNotEmpty(slug);
    }

    private void validateBackgroundType(CreatePageRequest createPageRequest) {
        if(!createPageRequest.getBackgroundType().equalsIgnoreCase(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR) &&
                !createPageRequest.getBackgroundType().equalsIgnoreCase(PAGE_BACKGROUND_TYPE_IMAGE)) {
            throw new RuleViolationException(
                    new Issue(ARGUMENT_NOT_VALID, INVALID_BACKGROUND_TYPE_ERROR)
            );
        }

        if(createPageRequest.getBackgroundType().equalsIgnoreCase(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR) &&
                !createPageRequest.getBackgroundValue().matches(HEX_VALIDATION_REGEX)){
            throw new RuleViolationException(
                    new Issue(ARGUMENT_NOT_VALID, INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR)
            );
        }

        if(createPageRequest.getBackgroundType().equalsIgnoreCase(PAGE_BACKGROUND_TYPE_IMAGE) &&
                !createPageRequest.getBackgroundValue().matches(URL_VALIDATION_REGEX)){
            throw new RuleViolationException(
                    new Issue(ARGUMENT_NOT_VALID, INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR)
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
