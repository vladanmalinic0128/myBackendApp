package com.userApp.backend.requests;

public record AuthenticationRequest(
        String username,
        String password
) {
}
