package com.cosmoram.application.controller;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.exception.ApplicationBadRequestException;
import com.cosmoram.application.service.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    public static final String MISSING_SESSION_ID_ERROR = "{\"session-id\":"
            + "[{\"field\":\"session-id\",\"error\":\"Required request header "
            + "'session-id' for method parameter type String is "
            + "not present\"}]}";

    public static final String MISSING_USER_ID_ERROR = "{\"user-id\":"
            + "[{\"field\""
            + ":\"user-id\",\"error\":\"Required request header 'user-id' for "
            + "method parameter type String is not present\"}]}";

    public static final String MISSING_CORRELATION_ID_ERROR =
            "{\"correlation-id\":[{\"field\":\"correlation-id\","
            + "\"error\":\"Required request header 'correlation-id' for method "
            + "parameter type String is not present\"}]}";

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
    public void testAddApplication() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performPositiveTest(application, status().is2xxSuccessful());
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    @Order(2)
    @DisplayName("Validation with No Code")
    public void testAddApplicationNoCode() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_NOT_BLANK));
    }

    @Test
    @Order(3)
    @DisplayName("Validation with No Name")
    public void testAddApplicationNoName() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_NOT_BLANK));
    }

    @Test
    @Order(4)
    @DisplayName("Validation with No Description")
    public void testAddApplicationNoDesc() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_NOT_BLANK));
    }

    @Test
    @Order(5)
    @DisplayName("Length validation for code. Less than Expected")
    public void testAddValidationCodeLessThanExpected() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("2000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_LESS_LENGTH));

    }

    @Test
    @Order(6)
    @DisplayName("Length validation for code. More than Expected")
    public void testAddValidationCodeMoreThanExpected() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("20002000000000000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_CODE_MORE_LENGTH));

    }

    @Test
    @Order(7)
    @DisplayName("Length validation for name. Less than Expected")
    public void testAddValidationNameLessThanExpected() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("Cr")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_LESS_LENGTH));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for name. More than Expected")
    public void testAddValidationNameMoreThanExpected() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("1234567890123456789012345678901234567890123456789012")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_NAME_MORE_LENGTH));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for Desc. Less than Expected")
    public void testAddValidationDescLessThanExpected() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_LESS_LENGTH));

    }

    /*@Test
    @Order(10)
    @DisplayName("Duplicate Request")
    public void testAddDuplicateRequest() throws Exception,
            ApplicationBadRequestException {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        when(applicationService.save(application)).
                thenThrow(new ApplicationBadRequestException
                        ("This code already exists"));
        mockMvc.perform(post(APPLICATION_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(application)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Message"));
    }*/

    @Test
    @Order(9)
    @DisplayName("Length validation for Desc. More than Expected")
    public void testAddValidationDescMoreThanExpected() throws Exception,
            ApplicationBadRequestException {
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

        performNegativeTest(application, status().isBadRequest(),
                content().string(ERROR_DESC_MORE_LENGTH));

    }

    private void performNegativeTest(Application application,
                                     ResultMatcher responseCode,
                                     ResultMatcher responseString)
            throws Exception, ApplicationBadRequestException {
        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                        .header(ApplicationController.HEADER_SESSION_ID,
                                "DummySession")
                        .header(ApplicationController.HEADER_USER_ID,
                                "DummyUser")
                        .header(ApplicationController.HEADER_CORRELATION_ID,
                                "DummyCorrelation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(application)))
                .andExpect(responseCode)
                .andExpect(responseString);
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    private void performPositiveTest(Application application,
                                     ResultMatcher responseCode)
            throws Exception, ApplicationBadRequestException {
        when(applicationService.save(application)).thenReturn(application);

        MvcResult mvcResult = mockMvc.perform(post(APPLICATION_URI)
                        .header(ApplicationController.HEADER_SESSION_ID,
                                "DummySession")
                        .header(ApplicationController.HEADER_USER_ID,
                                "DummyUser")
                        .header(ApplicationController.HEADER_CORRELATION_ID,
                                "DummyCorrelation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(application)))
                .andExpect(responseCode)
                .andReturn();

        Application returnedApplication = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Application>() {});

        Assertions.assertNotNull(returnedApplication.getId());
    }

    @Test
    @Order(10)
    @DisplayName("Header Validation with No Session Id")
    public void testHeaderNoSessionId() throws ApplicationBadRequestException,
            Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                        .header(ApplicationController.HEADER_USER_ID,
                                "DummyUser")
                        .header(ApplicationController.HEADER_CORRELATION_ID,
                                "DummyCorrelation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(application)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_SESSION_ID_ERROR));

    }

    @Test
    @Order(11)
    @DisplayName("Header Validation with No User Id")
    public void testHeaderNoUser() throws ApplicationBadRequestException,
            Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                .header(ApplicationController.HEADER_SESSION_ID,
                        "DummySession")
                .header(ApplicationController.HEADER_CORRELATION_ID,
                        "DummyCorrelation")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(toJson(application)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_USER_ID_ERROR));
    }

    @Test
    @Order(12)
    @DisplayName("Header Validation with No Correlation Id")
    public void testHeaderNoCorrelation() throws ApplicationBadRequestException,
            Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                .header(ApplicationController.HEADER_SESSION_ID,
                        "DummySession")
                .header(ApplicationController.HEADER_USER_ID,
                        "DummyUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(application)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(MISSING_CORRELATION_ID_ERROR));
    }
}
