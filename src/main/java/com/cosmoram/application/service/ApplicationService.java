package com.cosmoram.application.service;

import com.cosmoram.application.entity.Application;
import com.cosmoram.application.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application save(Application application) {
        System.out.println("In Service");
        return applicationRepository.save(application);
    }
}
