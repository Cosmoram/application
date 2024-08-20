package com.cosmoram.application.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationBadRequestException extends Throwable {
    private List<ApplicationError> errors;
}
