package com.userApp.backend.controllers.jwt;

import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.CommentRequest;
import com.userApp.backend.requests.FitnessProgramFilterRequest;
import com.userApp.backend.requests.PostFitnessProgramRequest;
import com.userApp.backend.responses.FitnessProgramResponse;
import com.userApp.backend.responses.LFitnessProgramResponse;
import com.userApp.backend.services.FitnessProgramService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/fitness-programs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtFitnessProgramController {
    private final FitnessProgramService fitnessProgramService;

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addNewComment(
            @RequestBody CommentRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        fitnessProgramService.createComment(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createFitnessProgram(
            @RequestBody PostFitnessProgramRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        fitnessProgramService.createFitnessProgram(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateFitnessProgram(
            @PathVariable String id,
            @RequestBody PostFitnessProgramRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        System.out.println(id);
        fitnessProgramService.updateFitnessProgram(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFitnessProgram(
            @PathVariable String id
    ) throws InvalidTokenException, EntityNotFoundException {
        fitnessProgramService.deleteFitnessProgram(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("finished/{id}")
    public ResponseEntity<?> markFitnessProgramAsFinished(
            @PathVariable String id
    ) throws InvalidTokenException, EntityNotFoundException {
        fitnessProgramService.markFitnessProgramAsFinished(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("status/{id}")
    public ResponseEntity<Boolean> getFitnessProgramStatus(
            @PathVariable String id
    ) throws InvalidTokenException, EntityNotFoundException {
        boolean status = fitnessProgramService.getStatus(id);
        return ResponseEntity.ok(status);
    }

}
