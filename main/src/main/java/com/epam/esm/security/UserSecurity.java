package com.epam.esm.security;

import com.epam.esm.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {
    @Autowired
    private UserDetailsService userDetailsService;

    public boolean hasUserId(Authentication authentication, long userId){
        String username = authentication.getName();
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        return userDetails.getId() == userId;
    }
}
