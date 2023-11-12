package com.buszta.cryptotracker.security;

import com.buszta.cryptotracker.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilis {
    static public User getUserFromSecurityContext(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
