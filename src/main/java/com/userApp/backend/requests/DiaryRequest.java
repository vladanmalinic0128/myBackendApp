package com.userApp.backend.requests;

public record DiaryRequest(
        Double currentWeight,
        String description,
        Integer trainingDuration
) {
}
