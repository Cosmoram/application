package com.cosmoram.application.controller;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.service.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Testing ApplicationController")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("checkstyle:MagicNumber")
public class ApplicationControllerTest {

    public static final String APPLICATION_URI = "/application/";

    public static final String ERROR_CODE_NOT_BLANK = "{\"errors\":"
            + "[{\"field\":\"code\",\"error\":\"must not be blank\"}]}";

    public static final String ERROR_NAME_NOT_BLANK = "{\"errors\":"
            + "[{\"field\":\"name\",\"error\":\"must not be blank\"}]}";

    public static final String ERROR_DESC_NOT_BLANK = "{\"errors\":"
            + "[{\"field\":\"desc\",\"error\":\"must not be blank\"}]}";

    public static final String ERROR_CODE_LESS_LENGTH = "{\"errors\":"
            + "[{\"field\":\"code\","
            + "\"error\":\"size must be between 9 and 9\"}]}";

    public static final String ERROR_CODE_MORE_LENGTH = "{\"errors\":"
            + "[{\"field\":\"code\","
            + "\"error\":\"size must be between 9 and 9\"}]}";

    public static final String ERROR_NAME_LESS_LENGTH = "{\"errors\":"
            + "[{\"field\":\"name\","
            + "\"error\":\"size must be between 3 and 50\"}]}";

    public static final String ERROR_NAME_MORE_LENGTH = "{\"errors\":"
            + "[{\"field\":\"name\","
            + "\"error\":\"size must be between 3 and 50\"}]}";

    public static final String ERROR_DESC_LESS_LENGTH = "{\"errors\":"
            + "[{\"field\":\"desc\","
            + "\"error\":\"size must be between 3 and 250\"}]}";

    public static final String ERROR_DESC_MORE_LENGTH = "{\"errors\":"
            + "[{\"field\":\"desc\","
            + "\"error\":\"size must be between 3 and 250\"}]}";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private ApplicationService applicationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    @DisplayName("Positive Scenario")
    public void testAddApplication() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        //performTest(application, status().is2xxSuccessful());
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @Order(2)
    @DisplayName("Validation with No Code")
    public void testAddApplicationNoCode() throws Exception {
        Application application = Application.builder()
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_NOT_BLANK));
    }

    @Test
    @Order(3)
    @DisplayName("Validation with No Name")
    public void testAddApplicationNoName() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_NOT_BLANK));
    }

    @Test
    @Order(4)
    @DisplayName("Validation with No Description")
    public void testAddApplicationNoDesc() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_NOT_BLANK));
    }

    @Test
    @Order(5)
    @DisplayName("Length validation for code. Less than Expected")
    public void testAddValidationCodeLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("2000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_LESS_LENGTH));

    }

    @Test
    @Order(6)
    @DisplayName("Length validation for code. More than Expected")
    public void testAddValidationCodeMoreThanExpected() throws Exception {
        Application application = Application.builder()
                .code("20002000000000000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_MORE_LENGTH));

    }

    @Test
    @Order(7)
    @DisplayName("Length validation for name. Less than Expected")
    public void testAddValidationNameLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Cr")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_LESS_LENGTH));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for name. More than Expected")
    public void testAddValidationNameMoreThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("1234567890123456789012345678901234567890123456789012")
                .desc("AR for NCL")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_MORE_LENGTH));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for Desc. Less than Expected")
    public void testAddValidationDescLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR")
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_LESS_LENGTH));

    }

    @Test
    @Order(9)
    @DisplayName("Length validation for Desc. More than Expected")
    public void testAddValidationDescMoreThanExpected() throws Exception {
        String veryLongString =
                "12345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890"
                + "12345678901234567890123456789012345678901234567890";

        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc(veryLongString)
                .build();

        performTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_MORE_LENGTH));

    }

    private void performTest(Application application,
                             ResultMatcher responseCode,
                             ResultMatcher responseString)
            throws Exception {
        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(application)))
                .andExpect(responseCode)
                .andExpect(responseString);
    }
}
