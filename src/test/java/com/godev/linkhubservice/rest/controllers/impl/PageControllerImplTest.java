package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.exceptions.Issue;
import com.godev.linkhubservice.domain.vo.CreatePageRequest;
import com.godev.linkhubservice.domain.vo.PageResponse;
import com.godev.linkhubservice.domain.vo.UpdatePageRequest;
import com.godev.linkhubservice.helpers.CreatePageRequestMockBuilder;
import com.godev.linkhubservice.helpers.PageResponseMockBuilder;
import com.godev.linkhubservice.helpers.UpdatePageRequestMockBuilder;
import com.godev.linkhubservice.security.jwt.JwtService;
import com.godev.linkhubservice.services.PageService;
import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static com.godev.linkhubservice.domain.constants.ValidationConstants.DESCRIPTION_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_FONT_COLOR_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.INVALID_URL_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.SLUG_REQUIRED_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.TITLE_LENGTH_ERROR;
import static com.godev.linkhubservice.domain.constants.ValidationConstants.URL_OR_HEX_FORMAT_ERROR;
import static com.godev.linkhubservice.domain.exceptions.IssueEnum.ARGUMENT_NOT_VALID;
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
    void pageControllerHappyPath() throws Exception{

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

    @Test
    @DisplayName("Should throw bad request when tittle field has invalid length")
    void tittleFieldHasInvalidLength() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTittle().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when description field has invalid length")
    void descriptionFieldHasInvalidLength() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthDescription().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, DESCRIPTION_LENGTH_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when photo field has invalid url format")
    void photoFieldHasInvalidUrlFormat() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidUrlFormatPhoto().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, INVALID_URL_FORMAT_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when font color field has invalid length")
    void fontColorFieldHasInvalidLength() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthFontColor().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when font color field has invalid rgb format")
    void fontColorFieldHasInvalidRgbFormat() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidRgbFormatFontColor().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when font color field has invalid name format")
    void fontColorFieldHasInvalidNameFormat() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidNameFormatFontColor().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when background value field has invalid rgb color format")
    void backgroundValueFieldHasInvalidRgbColorFormat() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidRgbFormatBackgroundValue().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, URL_OR_HEX_FORMAT_ERROR))));
    }

    @ParameterizedTest
    @MethodSource("pageRequestsInvalidFormats")
    void createPageInvalidFormats(CreatePageRequest createPageRequest, Issue issue) throws Exception {

        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    private static Stream<Arguments> pageRequestsInvalidFormats(){
        return Stream.of(
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_REQUIRED_ERROR)),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withEmptySlug().build(), new Issue(ARGUMENT_NOT_VALID, List.of(SLUG_REQUIRED_ERROR, SLUG_LENGTH_ERROR))),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR)),
                Arguments.of(CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTittle().build(), new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR))
        );

    }

    @ParameterizedTest
    @MethodSource("updateInvalidPageRequestsAndIssues")
    void updatePageInvalidFormats(UpdatePageRequest updatePageRequest, Issue issue) throws Exception {

        final var bearerToken = "Bearer kibe";

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    private static Stream<Arguments> updateInvalidPageRequestsAndIssues() {
        return Stream.of(
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build(), new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTitle().build(), new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthDescription().build(), new Issue(ARGUMENT_NOT_VALID, DESCRIPTION_LENGTH_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatPhoto().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_URL_FORMAT_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatBackgroundValue().build(), new Issue(ARGUMENT_NOT_VALID, URL_OR_HEX_FORMAT_ERROR))

        );
    }

    @ParameterizedTest
    @MethodSource("updateNullFieldsPageRequests")
    void updatePageNullFields(UpdatePageRequest updatePageRequest, PageResponse pageResponse) throws Exception {

        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    private static Stream<Arguments> updateNullFieldsPageRequests() {
        return Stream.of(
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullTitle().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullDescription().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build(), PageResponseMockBuilder.getBuilder().mock().build()),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundValue().build(), PageResponseMockBuilder.getBuilder().mock().build())
        );
    }

}
