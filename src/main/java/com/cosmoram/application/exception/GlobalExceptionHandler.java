package com.cosmoram.application.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public final class GlobalExceptionHandler {

    public static final String COSOMORAM_APPLICATION_REQUEST_ERROR
            = "cosomoram.application.request_error";
    public static final String COSMORAM_APPLICATION_HEADER_ERROR =
            "cosmoram.application.header_error";
    public static final String COSMORAM_APPLICATION_NOT_FOUND =
            "cosmoram.application.not_found";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleAnnotationErrors(MethodArgumentNotValidException ex) {
        List<ApplicationError> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> new ApplicationError(e.getField(),
                        COSOMORAM_APPLICATION_REQUEST_ERROR,
                        e.getDefaultMessage())).toList();

        Map<String, List<ApplicationError>> errorMap =
            new HashMap<String, List<ApplicationError>>();

        errorMap.put("errors", errors);

        return new ResponseEntity<>(errorMap, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationBadRequestException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleBadRequestException(ApplicationBadRequestException ex) {

        ex.getErrors()
                .forEach(e -> e.setErrorMessage(
                        messageSource.getMessage(e.getErrorCode(),
                                null, "Default Message",
                                LocaleContextHolder.getLocale())));
        Map<String, List<ApplicationError>> errorMap =
                Map.of("errors", ex.getErrors());

        return new ResponseEntity<>(errorMap, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleHeaderExceptions(MissingRequestHeaderException ex) {

        Map<String, List<ApplicationError>> errorMap = Map.of(
                "errors",
                List.of(new ApplicationError(ex.getHeaderName(),
                        COSMORAM_APPLICATION_HEADER_ERROR,
                        ex.getMessage()
        )));
        return new ResponseEntity<>(errorMap, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleResourceNotFoundException(NoResourceFoundException ex) {
        return new ResponseEntity<>(Map.of("errors",
                List.of(new ApplicationError("",
                        COSMORAM_APPLICATION_NOT_FOUND,
                        messageSource.getMessage(COSMORAM_APPLICATION_NOT_FOUND,
                                null, "Default Message",
                                LocaleContextHolder.getLocale())))),
                HttpStatus.NOT_FOUND);
    }

}
