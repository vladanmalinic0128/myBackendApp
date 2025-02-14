package com.userApp.backend.controllers.opened;

import com.userApp.backend.responses.CategoryResponse;
import com.userApp.backend.responses.CityResponse;
import com.userApp.backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping( "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCities(){
        List<CategoryResponse> categories = categoryService.getAvailableCategories();
        return ResponseEntity.ok(categories);
    }
}
