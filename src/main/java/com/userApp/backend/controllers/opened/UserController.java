package com.userApp.backend.controllers.opened;

import com.userApp.backend.exceptions.*;
import com.userApp.backend.requests.AuthenticationRequest;
import com.userApp.backend.requests.EmailExistsRequest;
import com.userApp.backend.requests.RegistrationRequest;
import com.userApp.backend.requests.UsernameExistsRequest;
import com.userApp.backend.responses.AuthenticationResponse;
import com.userApp.backend.responses.EmailExistsResponse;
import com.userApp.backend.responses.RegistrationResponse;
import com.userApp.backend.responses.UsernameExistsResponse;
import com.userApp.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

@RestController
@RequestMapping( "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @RequestBody RegistrationRequest request
    ) throws RegistrationException {
        RegistrationResponse result = userService.register(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/register/confirm")
    public String confirm(@RequestParam("token") String token) {
        return userService.confirmToken(token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) throws EmailNotVerifiedException, UsernameNotFoundException {
        return ResponseEntity.ok(userService.authenticate(request));
    }

    @PostMapping("/send-confirmation-mail")
    public ResponseEntity<AuthenticationResponse> sendConfirmationMail(
            @RequestBody AuthenticationRequest request
    ) throws UsernameNotFoundException, AccountLockedException, EmailAlreadyVerifiedException, WrongCredentialsException {
        return ResponseEntity.ok(userService.sendConfirmationMail(request));
    }

    @PostMapping("/username-exists")
    public ResponseEntity<UsernameExistsResponse> usernameExists(@RequestBody UsernameExistsRequest request) {
        return ResponseEntity.ok(userService.usernameExists(request));
    }

    @PostMapping("/email-exists")
    public ResponseEntity<EmailExistsResponse> emailExists(@RequestBody EmailExistsRequest request) {
        return ResponseEntity.ok(userService.emailExists(request));
    }
}
