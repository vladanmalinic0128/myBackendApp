package com.userApp.backend.controllers.opened;


import com.userApp.backend.responses.LocationResponse;
import com.userApp.backend.responses.SpecificAttributeResponse;
import com.userApp.backend.services.LocationService;
import com.userApp.backend.services.SpecificAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SpecificAttributesController {
    private final SpecificAttributeService specificAttributeService;

    @GetMapping("/categories/{id}/specificAttributes")
    public ResponseEntity<List<SpecificAttributeResponse>> getAllSpecificAttributesByCategory(@PathVariable String id){
        Long categoryId;
        try{
            categoryId = Long.parseLong(id);
        } catch(NumberFormatException ex) {
            return ResponseEntity.badRequest().build();
        }
        List<SpecificAttributeResponse> specificAttributes = specificAttributeService.getAllSpecificAttributesByCategory(categoryId);
        return ResponseEntity.ok(specificAttributes);
    }
}
