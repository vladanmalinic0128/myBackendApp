package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.repositories.AdministratorEntityRepository;
import com.userApp.backend.repositories.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdministratorService {
    private final AdministratorEntityRepository administratorRepository;
    private final UserEntityRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


}
