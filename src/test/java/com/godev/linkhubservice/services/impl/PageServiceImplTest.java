package com.godev.linkhubservice.services.impl;

import com.godev.linkhubservice.domain.exceptions.ForbiddenException;
import com.godev.linkhubservice.domain.exceptions.ObjectNotFoundException;
import com.godev.linkhubservice.domain.exceptions.RuleViolationException;
import com.godev.linkhubservice.domain.models.Account;
import com.godev.linkhubservice.domain.models.Page;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.helpers.AccountMockBuilder;
import com.godev.linkhubservice.helpers.CreatePageRequestMockBuilder;
import com.godev.linkhubservice.helpers.PageMockBuilder;
import com.godev.linkhubservice.helpers.UpdatePageRequestMockBuilder;
import com.godev.linkhubservice.services.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_TYPE_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_BACKGROUND_VALUE;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_FONT_COLOR;
import static com.godev.linkhubservice.domain.constants.DatabaseValuesConstants.DEFAULT_PAGE_PHOTO;
import static com.godev.linkhubservice.domain.constants.IssueDetails.ID_NOT_FOUND_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.SLUG_EXISTS_ERROR;
import static com.godev.linkhubservice.domain.constants.IssueDetails.USER_NOT_ALLOWED;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BACKGROUND_TYPE_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.ARGUMENT_NOT_VALID;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.FORBIDDEN;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.OBJECT_NOT_FOUND;
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

    private PageServiceImpl pageService;

    CreatePageRequest mockedCreatePageRequest;

    Account mockedAccount;

    Page mockedPageSaved;

    UserDetails userDetails = User.builder()
            .username("kibe@email.com")
            .password("Kibe@1234")
            .roles("USER")
            .build();

    UpdatePageRequest mockedUpdatePageRequest;

    @BeforeEach
    void setup(){
        this.pageService = new PageServiceImpl(pageRepository, accountService, new ModelMapper());

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(this.userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().build();
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().build();

        this.mockedAccount = AccountMockBuilder.getBuilder().mock().withId().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().build();

    }

    @Test
    @DisplayName("Should return Page Response when save success with backgroundType color.")
    void createPageHappyPath(){
        //arrange
        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
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
    @DisplayName("Should return Page Response when save success with backgroundType image.")
    void createPageHappyPath2(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withBackgroundTypeImage().withValidUrlBackgroundValue().build();

        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
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
    @DisplayName("Should throw Rule Violation exception when Slug already exists")
    void createPageSlugAlreadyExists(){
        //arrange
        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug()))
                .thenReturn(Optional.of(this.mockedPageSaved));

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.create(this.mockedCreatePageRequest));

        //assert
        Assertions.assertEquals(ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(SLUG_EXISTS_ERROR, this.mockedCreatePageRequest.getSlug())),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should return Page Response with default photo when Create Page Request has null Photo")
    void createPageNullPhoto(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultPhoto().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
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
    @DisplayName("Should return Page Response with default Font Color when Create Page Request has null Font Color")
    void createPageNullFontColor(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultFontColor().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
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
    @DisplayName("Should return Page Response with default Background Type Color when Create Page Request has null Background Type")
    void createPageNullBackgroundType(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultBackgroundTypeColor().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
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
    @Test
    @DisplayName("Should return Page Response with default Background Value when Create Page Request has null Background Value")
    void createPageNullBackgroundValue(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundValue().build();
        this.mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().withDefaultBackgroundValue().build();


        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.create(this.mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        Assertions.assertEquals(DEFAULT_PAGE_BACKGROUND_VALUE, pageResponse.getBackgroundValue());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(this.mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
    }

    @Test
    @DisplayName("Should throw Rule Violation exception when Background Type field is different of Color and Image")
    void createPageBackgroundTypeIsNotColorAndImage(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidBackgroundType().build();

        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.create(this.mockedCreatePageRequest));

        //assert
        Assertions.assertEquals(ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(INVALID_BACKGROUND_TYPE_ERROR),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should throw Rule Violation exception when Background type is selected as COLOR and Background value is not hex format")
    void createPageMismatchBackgroundTypeValue(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidRgbFormatBackgroundValue().build();

        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.create(this.mockedCreatePageRequest));

        //assert
        Assertions.assertEquals(ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(INVALID_BG_VALUE_FOR_BG_TYPE_COLOR_ERROR),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should throw Rule Violation exception when Background type is selected as IMAGE and Background value is not a url format valid")
    void createPageMismatchBackgroundTypeValue2(){
        //arrange
        this.mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withBackgroundTypeImage().withInvalidUrlFormatPhoto().build();

        when(this.accountService.findByEmail(this.userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findBySlug(this.mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.create(this.mockedCreatePageRequest));

        //assert
        Assertions.assertEquals(ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(INVALID_BG_VALUE_FOR_BG_TYPE_IMAGE_ERROR),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should return Page Response when update success with valid fields")
    void pageUpdateHappyPatch(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should throw Forbiden Exception when an user is not allowed editing page")
    void pageUpdateNotAllowed(){
        //arrange
        this.mockedAccount = AccountMockBuilder.getBuilder().mock().withAnotherId().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));

        //action
        ForbiddenException forbiddenException = Assertions.assertThrows(ForbiddenException.class,
                () -> this.pageService.update(this.mockedUpdatePageRequest, 1));

        //assertions
        Assertions.assertEquals(FORBIDDEN.getMessage(), forbiddenException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(USER_NOT_ALLOWED, this.mockedPageSaved.getSlug())),
                forbiddenException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should return Page Response when update success with null title")
    void pageUpdateNullTitle(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullTitle().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null description")
    void pageUpdateNullDescription(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullDescription().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null photo")
    void pageUpdateNullPhoto(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null font color")
    void pageUpdateNullFontColor(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null background type")
    void pageUpdateNullBackgroundType(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null background value")
    void pageUpdateNullBackgroundValue(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundValue().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should return Page Response when update success with null slug")
    void pageUpdateNullSlug(){
        //arrange
        this.mockedUpdatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.save(this.mockedPageSaved)).thenReturn(this.mockedPageSaved);

        //action
        final var pageResponse = this.pageService.update(this.mockedUpdatePageRequest, 1);

        //assertions
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(this.mockedPageSaved.getId(), pageResponse.getId());
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findById(1);
        verify(this.pageRepository, times(1)).save(this.mockedPageSaved);
    }

    @Test
    @DisplayName("Should throw Rule Violation Exception when slug already exists")
    void pageUpdateSlugAlreadyExists(){
        //arrange
        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.ofNullable(this.mockedPageSaved));
        when(this.pageRepository.findBySlug(this.mockedUpdatePageRequest.getSlug())).thenReturn(Optional.of(this.mockedPageSaved));

        //action
        RuleViolationException ruleViolationException = Assertions.assertThrows(RuleViolationException.class,
                () -> this.pageService.update(this.mockedUpdatePageRequest, 1));

        //assertions
        Assertions.assertEquals(ARGUMENT_NOT_VALID.getMessage(), ruleViolationException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(SLUG_EXISTS_ERROR, this.mockedUpdatePageRequest.getSlug())),
                ruleViolationException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should throw Object Not Found Exception when page is not found")
    void pageUpdateNotFoundPage(){
        //arrange
        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findById(1)).thenReturn(Optional.empty());

        //action
        ObjectNotFoundException objectNotFoundException = Assertions.assertThrows(ObjectNotFoundException.class,
                () -> this.pageService.update(this.mockedUpdatePageRequest, 1));

        //assertions
        Assertions.assertEquals(OBJECT_NOT_FOUND.getMessage(), objectNotFoundException.getIssue().getMessage());
        Assertions.assertEquals(List.of(String.format(ID_NOT_FOUND_ERROR, 1)),
                objectNotFoundException.getIssue().getDetails());
    }

    @Test
    @DisplayName("Should show a list from user pages")
    void findPagesByAccountIdHappyPatch(){
        //arrange
        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findPagesByAccount_Id(this.mockedAccount.getId())).thenReturn(List.of(this.mockedPageSaved));

        //action
        final var pageResponse = this.pageService.findPagesByAccountId();

        //assertions
        Assertions.assertNotNull(pageResponse);
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findPagesByAccount_Id(this.mockedAccount.getId());
    }

    @Test
    @DisplayName("Should show a empty list from user pages")
    void findPagesByAccountIdHappyPatchEmptyList(){
        //arrange
        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(this.mockedAccount);
        when(this.pageRepository.findPagesByAccount_Id(this.mockedAccount.getId())).thenReturn(Collections.emptyList());

        //action
        final var pageResponse = this.pageService.findPagesByAccountId();

        //assertions
        Assertions.assertNotNull(pageResponse);
        verify(this.accountService, times(1)).findByEmail(this.userDetails.getUsername());
        verify(this.pageRepository, times(1)).findPagesByAccount_Id(this.mockedAccount.getId());
    }
}