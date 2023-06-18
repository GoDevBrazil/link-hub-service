package com.godev.linkhubservice.domain.services.impl;

import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.services.AccountService;
import com.godev.linkhubservice.helpers.AccountResponseMockBuilder;
import com.godev.linkhubservice.helpers.CreatePageRequestMockBuilder;
import com.godev.linkhubservice.helpers.PageMockBuilder;
import org.junit.jupiter.api.Assertions;
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

import java.util.Optional;

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

    @Test
    void shouldReturnPageResponseWhenSaveSuccess(){
        //arrange
        UserDetails userDetails = User.builder()
                .username("kibe@email.com")
                .password("321")
                .roles("USER")
                .build();
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        final var mockedCreatePageRequest = CreatePageRequestMockBuilder.getBuilder().mock().build();
        final var mockedAccountResponse = AccountResponseMockBuilder.getBuilder().mock().build();
        final var mockedPageSaved = PageMockBuilder.getBuilder().mock().withId().build();

        when(this.accountService.findByEmail(userDetails.getUsername())).thenReturn(mockedAccountResponse);
        when(this.pageRepository.findBySlug(mockedCreatePageRequest.getSlug())).thenReturn(Optional.empty());
        when(this.pageRepository.save(any())).thenReturn(mockedPageSaved);

        //action
        final var pageResponse = pageService.create(mockedCreatePageRequest);

        //assert
        Assertions.assertNotNull(pageResponse);
        Assertions.assertEquals(mockedPageSaved.getId(), pageResponse.getId());
        verify(this.pageRepository, times(1)).save(any());
        verify(this.pageRepository, times(1)).findBySlug(mockedCreatePageRequest.getSlug());
        verify(this.accountService, times(1)).findByEmail(userDetails.getUsername());
    }
}