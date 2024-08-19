package com.cosmoram.application.service;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.exception.ApplicationBadRequestException;
import com.cosmoram.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public final class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application save(Application application) throws
            ApplicationBadRequestException {
        Optional<Application> existingApplication =
                applicationRepository.findByCode(application.getCode());
        if (existingApplication.isPresent())
            throw new ApplicationBadRequestException("This code already "
                    + "exists");

        return applicationRepository.save(application);
    }
}
