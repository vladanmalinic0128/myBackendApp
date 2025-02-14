package com.userApp.backend.requests;

public record ParticipationRequest(
        Long fitnessProgramId,
        Long paymentTypeId,
        String cardNumber
) {
}
