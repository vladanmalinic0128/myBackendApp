package com.userApp.backend.exceptions;

public class EmailAlreadyVerifiedException extends Exception {

    public EmailAlreadyVerifiedException() {
    }

    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
