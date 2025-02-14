package com.userApp.backend.controllers.jwt;

import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.FitnessProgramFilterRequest;
import com.userApp.backend.requests.ParticipationRequest;
import com.userApp.backend.responses.DiaryResponse;
import com.userApp.backend.responses.FitnessProgramResponse;
import com.userApp.backend.responses.ParticipationResponse;
import com.userApp.backend.services.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/participations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtParticipationController {
    private final ParticipationService participationService;


    @PostMapping
    public ResponseEntity<?> addNewParticipation(
            @RequestBody ParticipationRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        participationService.addNewParticipation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ParticipationResponse>> getAllParticipations() throws InvalidTokenException {
        List<ParticipationResponse> participations = participationService.getAllParticipations();
        return ResponseEntity.ok(participations);
    }
}
