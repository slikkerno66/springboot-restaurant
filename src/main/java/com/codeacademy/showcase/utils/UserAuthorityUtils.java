package com.codeacademy.showcase.utils;

import com.codeacademy.showcase.constant.ErrorCodeConstant;
import com.codeacademy.showcase.exception.RestaurantCustomException;
import com.codeacademy.showcase.utilenum.Role;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;

public class UserAuthorityUtils {

    public static Authentication getAuthenticateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication;
        }
        return null;
    }

    public static boolean isLogin() {
        Authentication authentication = getAuthenticateUser();
        return isLogin(authentication);
    }

    private static boolean isLogin(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    public static String getUsernameFromAuthentication() {
        Authentication authentication = getAuthenticateUser();
        if (isLogin(authentication)) {
            return authentication.getName();
        } else {
            throw new RestaurantCustomException(ErrorCodeConstant.REST_3, HttpStatus.UNAUTHORIZED);
        }
    }

    public static boolean isLoginAs(String role) {
        Authentication authentication = getAuthenticateUser();

        if (isLogin(authentication)) {
            return isContainRole(authentication.getAuthorities(), role);
        }

        return false;
    }

    public static boolean isContainRole(String authorities, String role) {
        String[] splitAuthorities = authorities.split(",");
        return Arrays.asList(splitAuthorities).contains(role);
    }

    public static boolean isContainRole(Collection<? extends GrantedAuthority> grantedAuthorities, String role) {
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            if (grantedAuthority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getRoles(String authorities) {
        if (authorities == null) {
            return new String[]{Role.ROLE_UNREGISTERED.name()};
        }
        return authorities.split(",");
    }

}
