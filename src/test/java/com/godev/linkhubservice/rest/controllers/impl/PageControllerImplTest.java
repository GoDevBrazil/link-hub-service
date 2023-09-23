package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.enums.PageFields;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.repository.AccountRepository;
import com.godev.linkhubservice.domain.repository.PageRepository;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.helpers.*;
import com.godev.linkhubservice.security.jwt.JwtService;
import com.godev.linkhubservice.services.PageService;
import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.godev.linkhubservice.domain.constants.IssueDetails.USER_NOT_ALLOWED;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.DESCRIPTION_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_FONT_COLOR_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_URL_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.URL_OR_HEX_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.ARGUMENT_NOT_VALID;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.FORBIDDEN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PageService pageService;

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PageRepository pageRepository;

    @MockBean
    private AccountRepository accountRepository;

    @BeforeEach
    void setup(){
        final var token = "kibe";
        final var userDetails = User.builder().username("kibe@email.com").password("123").roles("USER").build();

        Mockito.when(this.jwtService.isValidToken(token)).thenReturn(Boolean.TRUE);
        Mockito.when(this.jwtService.getLoggedAccount(token)).thenReturn("kibe@email.com");
        Mockito.when(this.accountService.loadUserByUsername("kibe@email.com")).thenReturn(userDetails);
    }

    @Test
    @DisplayName("Should register page when valid body is passed")
    void createPageHappyPath() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.create(createPageRequest)).thenReturn(pageResponse);

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @ParameterizedTest(name = "Should throw exception when invalid {2} is passed")
    @MethodSource("pageRequestsInvalidFormats")
    void createPageInvalidFormats(CreatePageRequest createPageRequest, Issue issue, PageFields pageFields) throws Exception {

        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    @Test
    @DisplayName("Should update page when valid body is passed")
    void updatePageHappyPath() throws Exception{

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

    }

    @ParameterizedTest(name = "Should throw exception when invalid {2} is passed")
    @MethodSource("updateInvalidPageRequestsAndIssues")
    void updatePageInvalidFormats(UpdatePageRequest updatePageRequest, Issue issue, PageFields pageFields) throws Exception {

        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    @ParameterizedTest(name = "Should update page with previous data when {2} is null ")
    @MethodSource("updateNullFieldsPageRequests")
    void updatePageNullFields(UpdatePageRequest updatePageRequest, PageResponse pageResponse, PageFields pageFields) throws Exception {

        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should show a list from user pages")
    void findPagesByAccountIdHappyPath() throws Exception{

        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.findPagesByAccountId()).thenReturn(List.of(pageResponse));

        mockMvc.perform(get("/page")
                        .contentType("application/json")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(pageResponse))));

    }

    @Test
    @DisplayName("Should show a empty list from user pages")
    void findPagesByAccountIdHappyPathEmptyList() throws Exception{

        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.findPagesByAccountId()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/page")
                        .contentType("application/json")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.emptyList())));

    }

    @Test
    @DisplayName("Should show a page response from user")
    void findByIdHappyPath() throws Exception{

        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.findById(1)).thenReturn(pageResponse);

        mockMvc.perform(get("/page/1")
                        .contentType("application/json")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

    }

    @Test
    @DisplayName("Should throw ForbiddenException when page id is of other user")
    void findByIdForbidden() throws Exception {

        final var page = PageMockBuilder.getBuilder().mock().withId().withAccountId(2).build();
        final var account = AccountMockBuilder.getBuilder().mock().withId().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.accountService.findByEmail("kibe@email.com")).thenReturn(account);
        Mockito.when(this.pageService.findPageById(1)).thenReturn(page);
        Mockito.when(this.pageService.validateAuthorizations(account, page));

        mockMvc.perform(get("/page/1")
                        .contentType("application/json")
                        .header("Authorization", bearerToken))
                .andExpect(status().isForbidden())
                .andExpect(content().json(objectMapper.writeValueAsString(new Issue(FORBIDDEN, String.format(USER_NOT_ALLOWED, 2)))));
    }

    private static Stream<Arguments> pageRequestsInvalidFormats(){
        return Stream.of(
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_REQUIRED_ERROR), PageFields.SLUG),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withEmptySlug().build(), new Issue(ARGUMENT_NOT_VALID, List.of(SLUG_REQUIRED_ERROR, SLUG_LENGTH_ERROR)), PageFields.SLUG),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR), PageFields.SLUG),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTittle().build(), new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR), PageFields.TITLE),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthDescription().build(), new Issue(ARGUMENT_NOT_VALID, DESCRIPTION_LENGTH_ERROR), PageFields.DESCRIPTION),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidUrlFormatPhoto().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_URL_FORMAT_ERROR), PageFields.PHOTO),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR), PageFields.FONTCOLOR),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidRgbFormatFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR), PageFields.FONTCOLOR),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidNameFormatFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR), PageFields.FONTCOLOR),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidRgbFormatBackgroundValue().build(), new Issue(ARGUMENT_NOT_VALID, URL_OR_HEX_FORMAT_ERROR), PageFields.BACKGROUNDVALUE)
        );

    }
    private static Stream<Arguments> updateInvalidPageRequestsAndIssues() {
        return Stream.of(
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR), PageFields.SLUG),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTitle().build(), new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR), PageFields.TITLE),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthDescription().build(), new Issue(ARGUMENT_NOT_VALID, DESCRIPTION_LENGTH_ERROR), PageFields.DESCRIPTION),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatPhoto().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_URL_FORMAT_ERROR), PageFields.PHOTO),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR), PageFields.FONTCOLOR),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatBackgroundValue().build(), new Issue(ARGUMENT_NOT_VALID, URL_OR_HEX_FORMAT_ERROR), PageFields.BACKGROUNDVALUE)

        );
    }
    private static Stream<Arguments> updateNullFieldsPageRequests() {
        return Stream.of(
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.SLUG),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullTitle().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.TITLE),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullDescription().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.DESCRIPTION),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.PHOTO),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.FONTCOLOR),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.BACKGROUNDTYPE),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundValue().build(), PageResponseMockBuilder.getBuilder().mock().build(), PageFields.BACKGROUNDVALUE)
        );
    }

}
