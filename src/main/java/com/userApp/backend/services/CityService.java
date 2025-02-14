package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.CityEntity;
import com.userApp.backend.repositories.CityRepository;
import com.userApp.backend.responses.CityResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    public List<CityResponse> getAllCities() {
        List<CityEntity> cities = cityRepository.findAll();
        return cities.stream().map(c -> modelMapper.map(c, CityResponse.class)).collect(Collectors.toList());
    }
}
