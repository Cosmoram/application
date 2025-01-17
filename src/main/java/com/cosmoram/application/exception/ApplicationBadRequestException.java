package com.cosmoram.application.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApplicationBadRequestException extends Exception {
    private final List<ApplicationError> errors;
}
