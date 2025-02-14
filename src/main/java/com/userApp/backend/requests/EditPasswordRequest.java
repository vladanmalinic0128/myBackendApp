package com.userApp.backend.requests;

public record EditPasswordRequest (
        String oldPassword,
        String newPassword
) {
}
