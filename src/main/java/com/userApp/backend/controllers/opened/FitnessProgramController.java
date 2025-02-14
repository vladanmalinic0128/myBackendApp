package com.userApp.backend.controllers.opened;

import com.userApp.backend.enums.FitnessProgramsRole;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.AuthenticationRequest;
import com.userApp.backend.requests.FitnessProgramFilterRequest;
import com.userApp.backend.responses.*;
import com.userApp.backend.services.FitnessProgramService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class FitnessProgramController {
    private final FitnessProgramService fitnessProgramService;
    @GetMapping("/fitness-programs")
    public ResponseEntity<List<FitnessProgramResponse>> getAllFitnessPrograms(
            @RequestParam(required = false) FitnessProgramsRole programTypes
    ) throws InvalidTokenException {
        System.out.println(programTypes);
        List<FitnessProgramResponse> fitnessPrograms = fitnessProgramService.getFitnessPrograms(programTypes);
        return ResponseEntity.ok(fitnessPrograms);
    }

    @PostMapping("/fitness-programs/filtered")
    public ResponseEntity<List<FitnessProgramResponse>> getFilteredFitnessPrograms(
            @RequestBody FitnessProgramFilterRequest request,
            @RequestParam(required = false) FitnessProgramsRole programTypes
    ) throws InvalidTokenException {
        System.out.println("Program types: " + programTypes);
        List<FitnessProgramResponse> fitnessPrograms = fitnessProgramService.getFilteredFitnessPrograms(request, programTypes);
        return ResponseEntity.ok(fitnessPrograms);
    }

    @GetMapping("/fitness-programs/{id}")
    @Transactional
    public ResponseEntity<LFitnessProgramResponse> getFitnessProgram(@PathVariable String id) throws EntityNotFoundException {
        LFitnessProgramResponse response = fitnessProgramService.getLFitnessProgram(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fitness-programs/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String id) throws EntityNotFoundException {
        List<CommentResponse> response = fitnessProgramService.getComments(id);
        return ResponseEntity.ok(response);
    }

}
