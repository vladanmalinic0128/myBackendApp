package com.userApp.backend.controllers.jwt;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.SubscriptionRequest;
import com.userApp.backend.responses.PaymentTypeResponse;
import com.userApp.backend.responses.SubscriptionResponse;
import com.userApp.backend.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/subscriptions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtSubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserValidator userValidator;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponse>> getAllSubscriptions() throws InvalidTokenException {
        userValidator.validateUser();
        List<SubscriptionResponse> paymentTypes = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(paymentTypes);
    }

    @PostMapping
    public ResponseEntity<?> changeSubscriptionStatus(
            @RequestBody SubscriptionRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        subscriptionService.changeSubscriptionStatus(request);
        return ResponseEntity.ok().build();
    }
}
