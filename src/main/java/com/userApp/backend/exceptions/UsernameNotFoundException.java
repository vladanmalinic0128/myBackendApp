package com.userApp.backend.exceptions;

public class UsernameNotFoundException extends Exception{
    public UsernameNotFoundException() {
    }

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
