package com.cosmoram.application.repository;

import com.cosmoram.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository
        extends JpaRepository<Application, UUID> {
    Optional<Application> findByCode(String code);
}
