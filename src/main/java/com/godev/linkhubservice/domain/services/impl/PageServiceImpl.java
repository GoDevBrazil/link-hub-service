package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.services.PageService;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final AccountRepository accountRepository;

    public PageServiceImpl(PageRepository pageRepository, AccountRepository accountRepository) {
        this.pageRepository = pageRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public PageResponse create(CreatePageRequest createPageRequest) {

        var userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = accountRepository.findByEmail(userDetails.getUsername());

        var page = Page
                .builder()
                .withAccount(account.get())
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
