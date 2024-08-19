package com.cosmoram.application.exception;

public class ApplicationBadRequestException extends Throwable {
    public ApplicationBadRequestException(String message) {
        super(message);
    }
}
