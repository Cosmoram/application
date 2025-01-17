package com.cosmoram.application.controller;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.exception.ApplicationBadRequestException;
import com.cosmoram.application.exception.ApplicationError;
import com.cosmoram.application.exception.GlobalExceptionHandler;
import com.cosmoram.application.service.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc
@WebMvcTest(value = ApplicationController.class)
@DisplayName("Testing ApplicationController")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("checkstyle:MagicNumber")
public class ApplicationControllerTest {

    public static final String APPLICATION_URI = "/application/v1";

    public static final String MUST_NOT_BE_BLANK = "must not be blank";
    public static final String DUPLICATE_REQUEST_MESSAGE = "{\"errors\":"
            + "[{\"field\":\"code\",\"errorCode\":\"code\",\"errorMessage\":"
            + "\"Default Message\"}]}";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private ApplicationController applicationController;

    @MockBean
    private ApplicationService applicationService;

    @Test
    @Order(0)
    @DisplayName("Validate Resource Not Found")
    void testResourceNotFound() throws Exception {
        mockMvc.perform(post("/dummyURL")
                .contentType(MediaType.APPLICATION_JSON)
                .content((new Application()).toJson()))
                .andExpect(status().isNotFound())
                .andExpect(content().string(getErrorMessageForStaticErrors(
                        "",
                        GlobalExceptionHandler.
                                COSMORAM_APPLICATION_NOT_FOUND,
                        "Resource not found")));
    }
    @Test
    @Order(1)
    @DisplayName("Positive Scenario")
    void testAddApplication() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performPositiveTest(application, status().is2xxSuccessful());
    }

    @Test
    @Order(2)
    @DisplayName("Validation with No Code")
    void testAddApplicationNoCode() throws Exception {
        Application application = Application.builder()
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("code",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        MUST_NOT_BE_BLANK)));
    }

    private static String getErrorMessageForStaticErrors(String field,
                                                         String errorCode,
                                                         String errorMessage)
            throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(
                Map.of("errors", List.of(ApplicationError.builder()
                        .field(field)
                        .errorCode(errorCode)
                        .errorMessage(errorMessage)
                        .build())));
    }

    @Test
    @Order(3)
    @DisplayName("Validation with No Name")
    void testAddApplicationNoName() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(
                        getErrorMessageForStaticErrors("name",
                                GlobalExceptionHandler.
                                        COSOMORAM_APPLICATION_REQUEST_ERROR,
                                MUST_NOT_BE_BLANK)));
    }

    @Test
    @Order(4)
    @DisplayName("Validation with No Description")
    void testAddApplicationNoDesc() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(
                        getErrorMessageForStaticErrors("desc",
                                GlobalExceptionHandler.
                                        COSOMORAM_APPLICATION_REQUEST_ERROR,
                                MUST_NOT_BE_BLANK)));
    }

    @Test
    @Order(5)
    @DisplayName("Length validation for code. Less than Expected")
    void testAddValidationCodeLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("2000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("code",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.CODE_LENGTH,
                                Application.CODE_LENGTH))));

    }

    private static String getMessageForSizeErrors(Integer lowerLimit,
                                                  Integer upperLimit) {
        return "size must be between "
                + lowerLimit
                + " and "
                + upperLimit;
    }

    @Test
    @Order(6)
    @DisplayName("Length validation for code. More than Expected")
    void testAddValidationCodeMoreThanExpected() throws Exception {
        Application application = Application.builder()
                .code("20002000000000000")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("code",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.CODE_LENGTH,
                                Application.CODE_LENGTH))));

    }

    @Test
    @Order(7)
    @DisplayName("Length validation for name. Less than Expected")
    void testAddValidationNameLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Cr")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("name",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.NAME_MIN_LENGTH,
                                Application.NAME_MAX_LENGTH))));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for name. More than Expected")
    void testAddValidationNameMoreThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("1234567890123456789012345678901234567890123456789012")
                .desc("AR for NCL")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("name",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.NAME_MIN_LENGTH,
                                Application.NAME_MAX_LENGTH))));

    }

    @Test
    @Order(8)
    @DisplayName("Length validation for Desc. Less than Expected")
    void testAddValidationDescLessThanExpected() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR")
                .build();

        performNegativeTest(application, status().isBadRequest(),
                content().string(getErrorMessageForStaticErrors("desc",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.DESC_MIN_LENGTH,
                                Application.DESC_MAX_LENGTH))));

    }

    //FIXME Replace Hard Coded Error Message
    @Test
    @Order(10)
    @DisplayName("Duplicate Request")
    void testAddDuplicateRequest() throws Exception {
        Application application = Application.builder()
                .code("200005405")
                .name("Credit Finance Edge")
                .desc("AR for NCL")
                .build();

        when(applicationService.save(application)).
                thenThrow(new ApplicationBadRequestException(
                        List.of(ApplicationError.builder()
                                .errorCode("code")
                                .errorMessage("Duplicate Request")
                                .field("code").build())));

        mockMvc.perform(post(APPLICATION_URI)
                .header(ApplicationController.HEADER_SESSION_ID,
                        "DummySession")
                .header(ApplicationController.HEADER_USER_ID,
                        "DummyUser")
                .header(ApplicationController.HEADER_CORRELATION_ID,
                        "DummyCorrelation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(application.toJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(DUPLICATE_REQUEST_MESSAGE));
    }

    @Test
    @Order(9)
    @DisplayName("Length validation for Desc. More than Expected")
    void testAddValidationDescMoreThanExpected() throws Exception {
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
                content().string(getErrorMessageForStaticErrors("desc",
                        GlobalExceptionHandler.
                                COSOMORAM_APPLICATION_REQUEST_ERROR,
                        getMessageForSizeErrors(Application.DESC_MIN_LENGTH,
                                Application.DESC_MAX_LENGTH))));

    }

    private void performNegativeTest(Application application,
                                     ResultMatcher responseCode,
                                     ResultMatcher responseString)
            throws Exception {
        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                        .header(ApplicationController.HEADER_SESSION_ID,
                                "DummySession")
                        .header(ApplicationController.HEADER_USER_ID,
                                "DummyUser")
                        .header(ApplicationController.HEADER_CORRELATION_ID,
                                "DummyCorrelation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(application.toJson()))
                .andExpect(responseCode)
                .andExpect(responseString);
    }

    private void performPositiveTest(Application application,
                                     ResultMatcher responseCode)
            throws Exception {
        when(applicationService.save(application)).thenReturn(application);

        mockMvc.perform(post(APPLICATION_URI)
                        .header(ApplicationController.HEADER_SESSION_ID,
                                "DummySession")
                        .header(ApplicationController.HEADER_USER_ID,
                                "DummyUser")
                        .header(ApplicationController.HEADER_CORRELATION_ID,
                                "DummyCorrelation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(application.toJson()))
                .andExpect(responseCode)
                .andReturn();
    }

    private static String getMessageForHeaderMandatoryErrors(String header) {
        return "Required request header '"
        + header
        + "' for method parameter type String is not present";
    }

    @Test
    @Order(11)
    @DisplayName("Header Validation with No User Id")
    void testHeaderNoUser() throws Exception {
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
                .contentType(application.toJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        getErrorMessageForStaticErrors(
                                ApplicationController.HEADER_USER_ID,
                        GlobalExceptionHandler.
                                COSMORAM_APPLICATION_HEADER_ERROR,
                        getMessageForHeaderMandatoryErrors(
                                ApplicationController.HEADER_USER_ID))));
    }

    @Test
    @Order(10)
    @DisplayName("Header Validation with No Session Id")
    void testHeaderNoSessionId() throws Exception {
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
                        .content(application.toJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        getErrorMessageForStaticErrors(
                                ApplicationController.HEADER_SESSION_ID,
                                GlobalExceptionHandler.
                                        COSMORAM_APPLICATION_HEADER_ERROR,
                                getMessageForHeaderMandatoryErrors(
                                        ApplicationController.HEADER_SESSION_ID))));

    }

    @Test
    @Order(12)
    @DisplayName("Header Validation with No Correlation Id")
    void testHeaderNoCorrelation() throws Exception {
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
                .content(application.toJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        getErrorMessageForStaticErrors(
                                ApplicationController.HEADER_CORRELATION_ID,
                        GlobalExceptionHandler.
                                COSMORAM_APPLICATION_HEADER_ERROR,
                        getMessageForHeaderMandatoryErrors(
                                ApplicationController.HEADER_CORRELATION_ID))));
    }
}
