package com.userApp.backend.services;

import com.userApp.backend.repositories.SpecificAttributeRepository;
import com.userApp.backend.responses.SpecificAttributeResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SpecificAttributeService {
    private final SpecificAttributeRepository specificAttributeRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public List<SpecificAttributeResponse> getAllSpecificAttributesByCategory(Long categoryId)
    {
        if(categoryService.existsById(categoryId) == false)
            return null;
        return specificAttributeRepository.findAllByCategory_Id(categoryId).stream().filter(sa -> sa.getIsActive())
                .map(sa -> modelMapper.map(sa, SpecificAttributeResponse.class)).collect(Collectors.toList());
    }
}
