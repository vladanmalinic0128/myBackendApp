package com.userApp.backend.controllers;

import com.userApp.backend.entitites.AdministratorEntity;
import com.userApp.backend.repositories.AdministratorEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    @Autowired
    private AdministratorEntityRepository repository;

    @GetMapping
    List<AdministratorEntity> findAll()
    {
        return repository.findAll();
    }
}
