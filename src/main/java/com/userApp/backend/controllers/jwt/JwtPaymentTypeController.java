package com.userApp.backend.controllers.jwt;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.responses.LocationResponse;
import com.userApp.backend.responses.PaymentTypeResponse;
import com.userApp.backend.services.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping( "/payment-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtPaymentTypeController {
    private final PaymentTypeService paymentTypeService;
    private final UserValidator userValidator;
    @GetMapping
    public ResponseEntity<List<PaymentTypeResponse>> getAllLocations() throws InvalidTokenException {
        userValidator.validateUser();
        List<PaymentTypeResponse> paymentTypes = paymentTypeService.getAllPaymentTypes();
        return ResponseEntity.ok(paymentTypes);
    }
}
