package com.godev.linkhubservice.rest.controllers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godev.linkhubservice.domain.exceptions.Issue;
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
    @DisplayName("Should throw bad request when slug field is null")
    void slugFieldIsNull() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, SLUG_REQUIRED_ERROR))));
    }

    @Test
    @DisplayName("Should throw bad request when slug field is empty")
    void slugFieldIsEmpty() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withEmptySlug().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, List.of(SLUG_REQUIRED_ERROR, SLUG_LENGTH_ERROR)))));
    }

    @Test
    @DisplayName("Should throw bad request when slug field has invalid length")
    void slugFieldHasInvalidLength() throws Exception{

        final var createPageRequest = CreatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build();
        final var bearerToken = "Bearer kibe";

        mockMvc.perform(post("/page")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createPageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR))));
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

    @Test
    @DisplayName("Should update page when valid body is passed")
    void updatePageHappyPath() throws Exception {

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

    @Test
    @DisplayName("Should update page when null slug is passed")
    void updatePageNullSlug() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullSlug().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should throw bad request when invalid length slug is passed")
    void updatePageInvalidLengthSlug() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthSlug().build();
        final var bearerToken = "Bearer kibe";


        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, SLUG_LENGTH_ERROR))));
    }

    @Test
    @DisplayName("Should update page when null title is passed")
    void updatePageNullTitle() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullTitle().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should throw bad request when invalid length title is passed")
    void updatePageInvalidLengthTitle() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthTitle().build();
        final var bearerToken = "Bearer kibe";


        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, TITLE_LENGTH_ERROR))));
    }

    @Test
    @DisplayName("Should update page when null description is passed")
    void updatePageNullDescription() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullDescription().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should throw bad request when invalid length description is passed")
    void updatePageInvalidLengthDescription() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidLengthDescription().build();
        final var bearerToken = "Bearer kibe";


        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, DESCRIPTION_LENGTH_ERROR))));
    }

    @Test
    @DisplayName("Should update page when null photo is passed")
    void updatePageNullPhoto() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullPhoto().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should throw bad request when invalid format photo is passed")
    void updatePageInvalidFormatPhoto() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatPhoto().build();
        final var bearerToken = "Bearer kibe";


        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new Issue(ARGUMENT_NOT_VALID, INVALID_URL_FORMAT_ERROR))));
    }

    @Test
    @DisplayName("Should update page when null font color is passed")
    void updatePageNullFontColor() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullFontColor().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should update page when null background type is passed")
    void updatePageNullBackgroundType() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundType().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @Test
    @DisplayName("Should update page when null background value is passed")
    void updatePageNullBackgroundValue() throws Exception {

        final var updatePageRequest = UpdatePageRequestMockBuilder.getBuilder().mock().withNullBackgroundValue().build();
        final var pageResponse = PageResponseMockBuilder.getBuilder().mock().build();
        final var bearerToken = "Bearer kibe";

        Mockito.when(this.pageService.update(updatePageRequest, 1)).thenReturn(pageResponse);

        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));
    }

    @ParameterizedTest
    @MethodSource("updatePageRequestAndIssues")
    void updatePageParametrized(UpdatePageRequest updatePageRequest, Issue issue) throws Exception {

        final var bearerToken = "Bearer kibe";


        mockMvc.perform(put("/page/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatePageRequest)).header("Authorization", bearerToken))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(issue)));
    }

    private static Stream<Arguments> updatePageRequestAndIssues() {
        return Stream.of(
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatBackgroundValue().build(), new Issue(ARGUMENT_NOT_VALID, URL_OR_HEX_FORMAT_ERROR)),
                Arguments.of(UpdatePageRequestMockBuilder.getBuilder().mock().withInvalidFormatFontColor().build(), new Issue(ARGUMENT_NOT_VALID, INVALID_FONT_COLOR_FORMAT_ERROR))

        );
    }

}
