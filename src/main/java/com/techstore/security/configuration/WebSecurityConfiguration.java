package com.techstore.security.configuration;

import com.techstore.repository.IPasswordResetTokenRepository;
import com.techstore.repository.IRegisterConfirmationTokenRepository;
import com.techstore.repository.IUserRepository;
import com.techstore.security.filter.CustomAuthenticationFilter;
import com.techstore.security.filter.CustomAuthorizationFilter;
import com.techstore.service.access.AccessControlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.techstore.constants.ApiConstants.ALL_CART_URLS_MATCHER;
import static com.techstore.constants.ApiConstants.ALL_FAVORITES_URLS_MATCHER;
import static com.techstore.constants.ApiConstants.ALL_ORDERS_URLS_MATCHER;
import static com.techstore.constants.ApiConstants.BASE_API_URL_MATCHER;
import static com.techstore.constants.ApiConstants.FULL_CONFIRM_REGISTER_URL;
import static com.techstore.constants.ApiConstants.FULL_FORGOT_PASSWORD_URL;
import static com.techstore.constants.ApiConstants.FULL_REFRESH_TOKEN_URL;
import static com.techstore.constants.ApiConstants.FULL_REGISTER_URL;
import static com.techstore.constants.ApiConstants.FULL_RESET_PASSWORD_URL;
import static com.techstore.constants.ApiConstants.LOGIN_URL;
import static com.techstore.constants.ApiConstants.PRODUCTS_URL;
import static com.techstore.constants.ApiConstants.PRODUCTS_WITH_PARAMS_REGEX;
import static com.techstore.constants.ApiConstants.PRODUCT_WITH_NAME_PARAM_REGEX;
import static com.techstore.constants.ApiConstants.PRODUCT_WITH_NAME_PATH_VARIABLE;
import static com.techstore.constants.ApiConstants.USER_GET_URL;
import static com.techstore.constants.RoleConstants.ROLE_ADMIN;
import static com.techstore.constants.RoleConstants.ROLE_CUSTOMER;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@Configuration
public class WebSecurityConfiguration {
    private static final List<String> ORIGINS = List.of("*");
    private static final List<String> METHODS = List.of("POST", "GET", "PUT", "DELETE");
    private static final List<String> HEADERS = List.of("*");

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRegisterConfirmationTokenRepository registerConfirmationTokenRepository;

    @Autowired
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Bean("password-encoder2")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("access-control-service")
    public AccessControlService accessControlService() {
        return new AccessControlService(jwtSecret, userRepository, registerConfirmationTokenRepository, passwordResetTokenRepository,
                passwordEncoder());
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
                .userDetailsService(accessControlService())
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean("custom-authentication-filter")
    public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager(http));
        customAuthenticationFilter.setUserRepository(userRepository);
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
                    auth.antMatchers(POST, LOGIN_URL, FULL_REGISTER_URL, FULL_REFRESH_TOKEN_URL, FULL_FORGOT_PASSWORD_URL).permitAll();
                    auth.antMatchers(PUT, FULL_RESET_PASSWORD_URL).permitAll();
                    auth.antMatchers(GET, PRODUCT_WITH_NAME_PATH_VARIABLE, FULL_CONFIRM_REGISTER_URL + "/**").permitAll();
                    auth.regexMatchers(GET, PRODUCTS_WITH_PARAMS_REGEX).permitAll();
                    auth.antMatchers(ALL_CART_URLS_MATCHER).permitAll();
                    auth.antMatchers(ALL_ORDERS_URLS_MATCHER, ALL_FAVORITES_URLS_MATCHER).hasAnyAuthority(ROLE_ADMIN, ROLE_CUSTOMER);
                    auth.antMatchers(GET, PRODUCTS_URL, PRODUCTS_URL + "/**").hasAnyAuthority(ROLE_ADMIN, ROLE_CUSTOMER);
                    auth.antMatchers(POST, USER_GET_URL, USER_GET_URL + "/").hasAnyAuthority(ROLE_ADMIN, ROLE_CUSTOMER);
                    auth.regexMatchers(PUT, PRODUCT_WITH_NAME_PARAM_REGEX).hasAnyAuthority(ROLE_ADMIN);
                    auth.antMatchers(BASE_API_URL_MATCHER).hasAuthority(ROLE_ADMIN);
                    auth.anyRequest().authenticated();
                })
                .addFilter(customAuthenticationFilter(http))
                .addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
