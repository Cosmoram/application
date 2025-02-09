package com.cosmoram.application.controller;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.exception.ApplicationBadRequestException;
import com.cosmoram.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO Implement i18n for headers
//TODO Add Tests for i18n
//TODO Implement documentation
//TODO i18n from database and cache
//TODO Implement in memory cache
//TODO Implement distributed cache
//TODO Implement Spring Security
@RestController
@RequestMapping(path = "/application")
public final class ApplicationController {

    public static final String HEADER_USER_ID = "user-id";
    public static final String HEADER_SESSION_ID = "session-id";
    public static final String HEADER_CORRELATION_ID = "correlation-id";
    public static final String VERSION = "version";

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(path = "/health")
    public ResponseEntity<Application> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{version}")
    public ResponseEntity<Application> addApplication(
            @RequestHeader(value = HEADER_USER_ID, required = true)
            String userId,

            @RequestHeader(value = HEADER_SESSION_ID, required = true)
            String sessionId,

            @RequestHeader(value = HEADER_CORRELATION_ID, required = true)
            String correlationId,

            @PathVariable(value = VERSION)
            String version,

            @Valid @RequestBody Application application)
            throws ApplicationBadRequestException {
        applicationService.validateHeaders(sessionId, userId, correlationId);
        return new ResponseEntity<>(applicationService.save(application),
                HttpStatus.CREATED);
    }
}
