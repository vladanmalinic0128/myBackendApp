package com.userApp.backend.controllers.jwt;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.responses.ChatUsersResponse;
import com.userApp.backend.responses.SubscriptionResponse;
import com.userApp.backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping( "/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtUserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<ChatUsersResponse> getAllUsers() throws InvalidTokenException {
        userValidator.validateUser();
        ChatUsersResponse chatUsers = userService.getUsersAndAdvisors();
        return ResponseEntity.ok(chatUsers);
    }

}
