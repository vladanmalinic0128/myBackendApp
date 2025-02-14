package com.userApp.backend.controllers.jwt;


import com.userApp.backend.config.UserValidator;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.MessageRequest;
import com.userApp.backend.responses.MessageResponse;
import com.userApp.backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtMessageController {
    private final MessageService messageService;
    private final UserValidator userValidator;

    @PostMapping
    public ResponseEntity<?> sendMessage(
            @RequestBody MessageRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        messageService.createMessage(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MessageResponse>> getAllMessages() throws InvalidTokenException {
        userValidator.validateUser();
        List<MessageResponse> messages = messageService.getAllMessages() ;
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<?> updateMessageState(@PathVariable Long messageId) throws InvalidTokenException, EntityNotFoundException {
        messageService.updateMessageState(messageId);
        return ResponseEntity.ok().build();
    }


}
