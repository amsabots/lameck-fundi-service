package com.amsabots.jenzi.fundi_service.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author andrew mititi on Date 12/31/21
 * @Project lameck-fundi-service
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    public Optional<String> getCurrentAuditor() {
        return Optional.of("user");

    }
}

