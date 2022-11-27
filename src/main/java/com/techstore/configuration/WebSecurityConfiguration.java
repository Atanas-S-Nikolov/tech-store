package com.techstore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.techstore.constants.RoleConstants.ROLE_ADMIN;
import static com.techstore.constants.RoleConstants.ROLE_CUSTOMER;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    private static final List<String> ORIGINS = Collections.singletonList("http://localhost:4200");
    private static final List<String> METHODS = Arrays.asList("POST", "GET", "PUT", "DELETE");
    private static final List<String> HEADERS = Collections.singletonList("*");

    @Bean("security-filter-chain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .build();
    }

    @Bean("cors-config-source")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ORIGINS);
        configuration.setAllowedMethods(METHODS);
        configuration.setAllowedHeaders(HEADERS);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean("role-hierarchy")
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl r = new RoleHierarchyImpl();
        r.setHierarchy(ROLE_ADMIN + " > " + ROLE_CUSTOMER);
        return r;
    }
}
