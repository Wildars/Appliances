package com.example.appliances.service.impl;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


public class SpringSecurityAuditorAwareServiceImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String by = "system";

        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            by = userDetails.getUsername();
        }

        return Optional.ofNullable(by);
    }
}
