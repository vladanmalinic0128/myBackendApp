package com.userApp.backend.controllers.opened;

import com.userApp.backend.responses.CityResponse;
import com.userApp.backend.services.CityService;
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
public class CityController {
    private final CityService cityService;

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getAllCities(){
        List<CityResponse> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }
}
