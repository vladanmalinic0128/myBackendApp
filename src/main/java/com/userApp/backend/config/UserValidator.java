package com.userApp.backend.config;


import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.AppUserEntityRepository;
import com.userApp.backend.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final AppUserEntityRepository appUserEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public UserEntity validateUser() throws InvalidTokenException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.isAuthenticated() == false)
            throw new InvalidTokenException("Invalid token");

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails == false)
            throw new InvalidTokenException("Invalid token");
        UserDetails userDetails = ((UserDetails) principal);
        String username = userDetails.getUsername();
        if (userDetails.isEnabled() == false)
            throw new InvalidTokenException("User is disabled");
        else if (userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked() && userDetails.isEnabled()){
            Optional<UserEntity> optionalUser = userEntityRepository.findByAppUser_Username(username);
            if(optionalUser.isPresent() == false)
                throw new InvalidTokenException("Cannot find user");
            else
                return optionalUser.get();
        }
        else
            throw new InvalidTokenException("Token is either expired or user is locked");

    }
}
