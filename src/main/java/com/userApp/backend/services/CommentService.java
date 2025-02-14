package com.userApp.backend.services;


import com.userApp.backend.config.UserValidator;
import com.userApp.backend.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


}
