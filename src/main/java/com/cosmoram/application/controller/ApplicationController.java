package com.cosmoram.application.controller;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/application/")
public final class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping(path = "/health")
    public ResponseEntity<Application> health() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Application> addApplication(
            @Valid @RequestBody Application application) {
        return new ResponseEntity<>(applicationService.save(application),
                HttpStatus.CREATED);
    }
}
