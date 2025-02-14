package com.userApp.backend.services;

import com.userApp.backend.entitites.AppUserEntity;
import com.userApp.backend.entitites.ConfirmationTokenEntity;
import com.userApp.backend.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationTokenEntity token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationTokenEntity> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

    public ConfirmationTokenEntity getTokenByUser(AppUserEntity user)
    {
        return confirmationTokenRepository.findById(user.getId()).orElseThrow(() -> {
            throw new IllegalStateException("Korisnik ne postoji");
        });
    }

}