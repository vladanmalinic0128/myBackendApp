package com.userApp.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationResponse {
    private String fitnessProgramName;
    private String paymentType;
    private String createdAt;
    private String cardNumber;
}
