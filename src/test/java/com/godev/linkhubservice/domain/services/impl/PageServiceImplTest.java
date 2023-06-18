package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.exceptions.IssueEnum;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.domain.vo.AccountResponse;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.helpers.AccountResponseMockBuilder;
import com.godev.linkhubservice.helpers.CreatePageRequestMockBuilder;
import com.godev.linkhubservice.helpers.PageMockBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_TYPE_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_FONT_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_PHOTO;
import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PageServiceImplTest {

    @Mock
    private PageRepository pageRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private PageServiceImpl pageService;

    CreatePageRequest mockedCreatePageRequest;
    AccountResponse mockedAccountResponse;
    Page mockedPageSaved;
    UserDetails userDetails = User.builder()
            .username("kibe@email.com")
            .password("321")
            .roles("USER")
            .build();

    @BeforeEach
    void setup(){
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(this.userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().build();
        this.mockedAccountResponse = AccountResponseMockBuilder.getBuilder().mock().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().build();
    }

    @Test
    void shouldReturnPageResponseWhenSaveSuccess(){
        //arrange
        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccountResponse);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.create(this.mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(this.mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
    }

    @Test
    void shouldThrowRuleViolationExceptionWhenSlugAlreadyExists(){
        //arrange
        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccountResponse);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug()))
                .thenReturn(Optional.of(this.mockedPageSaved));

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.create(this.mockedCreatePageRequest));

        //assert
        Assertions.assertEquals(IssueEnum.ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(SLUG_EXISTS_ERROR, this.mockedCreatePageRequest.getSlug())),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    void shouldReturnPageResponseWithDefaultPhotoWhenCreatePageRequestHasNullPhoto(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultPhoto().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccountResponse);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.create(this.mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        Assertions.assertEquals(DEFAULT_PAGE_PHOTO, pageResponse.getPhoto());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(this.mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
    }

    @Test
    void shouldReturnPageResponseWithDefaultFontColorWhenCreatePageRequestHasNullFontColor(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultFontColor().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccountResponse);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.create(this.mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        Assertions.assertEquals(DEFAULT_PAGE_FONT_COLOR, pageResponse.getFontColor());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(this.mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
    }

    @Test
    void shouldReturnPageResponseWithDefaultBackgroundTypeColorWhenCreatePageRequestHasNullBackgroundType(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultBackgroundTypeColor().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccountResponse);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.create(this.mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        Assertions.assertEquals(DEFAULT_PAGE_BACKGROUND_TYPE_COLOR, pageResponse.getBackgroundType());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(this.mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
    }
}