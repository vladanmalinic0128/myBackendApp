package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.CategoryEntity;
import com.userApp.backend.entitites.FitnessProgramEntity;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.repositories.CategoryRepository;
import com.userApp.backend.responses.CategoryResponse;
import com.userApp.backend.responses.LFitnessProgramResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


    public List<CategoryResponse> getAvailableCategories() {
        return categoryRepository.findAll().stream().filter(c -> c.getIsActive()).map(c -> modelMapper.map(c, CategoryResponse.class)).collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    public CategoryEntity findById(Long id) throws EntityNotFoundException {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("CategoryEntity");
        return optional.get();
    }

}
