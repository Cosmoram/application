package com.cosmoram.application.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public final class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleAnnotationErrors(MethodArgumentNotValidException ex) {
        List<ApplicationError> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(e -> new ApplicationError(e.getField(),
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

        Map<String, List<ApplicationError>> errorMap =
                Map.of("errors", ex.getErrors());

        return new ResponseEntity<>(errorMap, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, List<ApplicationError>>>
    handleHeaderExceptions(MissingRequestHeaderException ex) {

        Map<String, List<ApplicationError>> errorMap = Map.of(
                ex.getHeaderName(),
                List.of(new ApplicationError(ex.getHeaderName(), ex.getMessage()
        )));
        return new ResponseEntity<>(errorMap, new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }


}
