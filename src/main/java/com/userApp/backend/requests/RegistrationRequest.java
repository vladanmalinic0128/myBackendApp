package com.userApp.backend.requests;

public record RegistrationRequest(
        String firstname,
        String lastname,
        String username,
        String password,
        String email,
        Long cityId,
        String avatar
) {
}
