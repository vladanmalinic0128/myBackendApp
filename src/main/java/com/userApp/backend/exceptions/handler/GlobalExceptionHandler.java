package com.userApp.backend.exceptions.handler;

import com.userApp.backend.entitites.LogEntity;
import com.userApp.backend.exceptions.*;
import com.userApp.backend.services.LogService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountLockedException;
import java.sql.Timestamp;

@AllArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final LogService logService;

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        String errorMessage = "Registration failed: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<String> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        String errorMessage = "Authentication failed: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String errorMessage = "Username not found: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<String> handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException ex) {
        String errorMessage = "Email already verified: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<String> handleAccountLockedException(AccountLockedException ex) {
        String errorMessage = "Account locked: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.LOCKED);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<String> handleAccountLockedException(WrongCredentialsException ex) {
        String errorMessage = "Account locked: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<String> handleAccountLockedException(InvalidTokenException ex) {
        String errorMessage = "Authorization failed: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleAccountLockedException(EntityNotFoundException ex) {
        String errorMessage = "Entity not found: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        String errorMessage = "Action failed: " + ex.getMessage();
        logService.logMessage(errorMessage);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
