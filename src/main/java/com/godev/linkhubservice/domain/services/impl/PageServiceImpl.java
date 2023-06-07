package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.domain.services.PageService;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final AccountService accountService;

    public PageServiceImpl(PageRepository pageRepository, AccountService accountService) {
        this.pageRepository = pageRepository;
        this.accountService = accountService;
    }

    @Override
    public PageResponse create(CreatePageRequest createPageRequest) {

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var accountResponse = accountService.findByEmail(userDetails.getUsername());
        var account = new Account();
        account.setId(accountResponse.getId());

        this.pageRepository.findBySlug(createPageRequest.getSlug())
                .ifPresent(page -> {
                    throw new RuleViolationException(
                            new Issue(IssueEnum.ARGUMENT_NOT_VALID, String.format(SLUG_EXISTS_ERROR, createPageRequest.getSlug()))
                    );
                });

        if(ObjectUtils.isEmpty(createPageRequest.getPhoto())){
            createPageRequest.setPhoto("avatar.png");
        }

        if(ObjectUtils.isEmpty(createPageRequest.getFontColor()) || createPageRequest.getFontColor() == null){
            createPageRequest.setFontColor("#212121");
        }

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
}
