package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.repositories.AppUserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "Username not found";
    private final AppUserEntityRepository appUserRepository;
    private final UserValidator userValidator;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
