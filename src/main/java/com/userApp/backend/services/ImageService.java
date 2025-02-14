package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


}
