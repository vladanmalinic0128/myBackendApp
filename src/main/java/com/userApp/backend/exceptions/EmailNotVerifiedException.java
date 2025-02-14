package com.userApp.backend.exceptions;

public class EmailNotVerifiedException extends Exception {

    public EmailNotVerifiedException() {}

    public EmailNotVerifiedException(String message) {
        super(message);
    }

}
