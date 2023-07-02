package com.techstore.utils.auth;

import com.techstore.exception.auth.CustomAuthorizationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.techstore.constants.RoleConstants.ROLE_ADMIN;
import static java.util.stream.Collectors.toList;

public class AuthUtils {
    private static final String ANONYMOUS_USER = "anonymousUser";

    public static void checkOwner(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userDetailsString = authentication.getName();
        List<String> roles = convertAuthoritiesToStrings(authentication);
        if (!userDetailsString.contains(username) && !roles.contains(ROLE_ADMIN)){
            throw new CustomAuthorizationException("This user is not supposed to view/edit this resource");
        }
    }

    public static void checkCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("You should have an account to view this resource");
        }
    }

    public static List<String> convertAuthoritiesToStrings(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList());
    }

    public static List<GrantedAuthority> convertStringsToAuthorities(List<String> authoritiesStrings) {
        return authoritiesStrings.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }
}
