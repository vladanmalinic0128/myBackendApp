package com.userApp.backend.controllers.jwt;


import com.userApp.backend.config.UserValidator;
import com.userApp.backend.exceptions.EmailNotVerifiedException;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.exceptions.RegistrationException;
import com.userApp.backend.requests.DiaryRequest;
import com.userApp.backend.requests.EditDataRequest;
import com.userApp.backend.requests.EditPasswordRequest;
import com.userApp.backend.services.DiaryService;
import com.userApp.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtSettingsController {
    private final UserService userService;
    private final UserValidator userValidator;

    @PostMapping("/edit-data")
    public ResponseEntity<?> editData(
            @RequestBody EditDataRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        userService.editData(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit-password")
    public ResponseEntity<?> editPassword(
            @RequestBody EditPasswordRequest request
    ) throws InvalidTokenException, EmailNotVerifiedException, RegistrationException {
        userService.editPassword(request);
        return ResponseEntity.ok().build();
    }
}
