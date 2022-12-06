package com.techstore.security.configuration;

import com.techstore.repository.IUserRepository;
import com.techstore.security.filter.CustomAuthenticationFilter;
import com.techstore.security.filter.CustomAuthorizationFilter;
import com.techstore.service.user.AbstractUserService;
import com.techstore.service.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.techstore.constants.ApiConstants.BASE_API_URL;
import static com.techstore.constants.ApiConstants.FULL_REFRESH_TOKEN_URL;
import static com.techstore.constants.ApiConstants.LOGIN_URL;
import static com.techstore.constants.ApiConstants.PRODUCTS_EARLY_ACCESS_FALSE_REGEX;
import static com.techstore.constants.ApiConstants.PRODUCTS_URL;
import static com.techstore.constants.ApiConstants.PRODUCT_WITH_PATH_VARIABLE_REGEX;
import static com.techstore.constants.ApiConstants.USERS_URL;
import static com.techstore.constants.RoleConstants.ROLE_ADMIN;
import static com.techstore.constants.RoleConstants.ROLE_CUSTOMER;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {
    private static final List<String> ORIGINS = Collections.singletonList("*");
    private static final List<String> METHODS = Arrays.asList("POST", "GET", "PUT", "DELETE");
    private static final List<String> HEADERS = Collections.singletonList("*");

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private IUserRepository userRepository;

    @Bean("password-encoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("user-service")
    public AbstractUserService userService() {
        return new UserService(userRepository, passwordEncoder(), jwtSecret);
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

    @Bean("authentication-manager")
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService())
                .and()
                .build();
    }

    @Bean("custom-authentication-filter")
    public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager(http));
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        return customAuthenticationFilter;
    }

    @Bean("custom-authorization-filter")
    public CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter();
    }

    @Bean("security-filter-chain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests(auth -> {
                    auth.antMatchers(POST, LOGIN_URL, USERS_URL).permitAll();
                    auth.antMatchers(GET, FULL_REFRESH_TOKEN_URL).permitAll();
                    auth.regexMatchers(GET, PRODUCTS_EARLY_ACCESS_FALSE_REGEX, PRODUCT_WITH_PATH_VARIABLE_REGEX).permitAll();
                    auth.antMatchers(GET, PRODUCTS_URL, PRODUCTS_URL + "/").hasAnyAuthority(ROLE_ADMIN, ROLE_CUSTOMER);
                    auth.antMatchers(BASE_API_URL + "/**").hasAuthority(ROLE_ADMIN);
                    auth.anyRequest().authenticated();
                })
                .addFilter(customAuthenticationFilter(http))
                .addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
