package com.userApp.backend.services;

import com.userApp.backend.repositories.FitnessProgramSpecialAttributeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FitnessProgramSpecialAttributeService {
    private final FitnessProgramSpecialAttributeRepository fitnessProgramSpecialAttributeRepository;
}
