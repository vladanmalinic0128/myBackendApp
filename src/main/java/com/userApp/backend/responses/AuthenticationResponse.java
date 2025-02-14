package com.userApp.backend.responses;

public record AuthenticationResponse(
        String token,
        String username,
        String email,
        String firstname,
        String lastname,
        String avatar,
        String city
) {
}
