package com.userApp.backend.services;

import com.userApp.backend.entitites.CategoryEntity;
import com.userApp.backend.entitites.LocationEntity;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.repositories.LocationRepository;
import com.userApp.backend.responses.CategoryResponse;
import com.userApp.backend.responses.LocationResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll().stream().map(l -> modelMapper.map(l, LocationResponse.class)).collect(Collectors.toList());
    }


    public LocationEntity findById(Long id) throws EntityNotFoundException {
        Optional<LocationEntity> optional = locationRepository.findById(id);
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("LocationEntity");
        return optional.get();
    }
}
