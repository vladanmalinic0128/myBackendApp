package com.userApp.backend.requests;

public record FitnessProgramFilterRequest(
        String name,
        Long categoryId,
        Long locationId,
        Integer startWeightLevel,
        Integer endWeightLevel,
        Integer startPrice,
        Integer endPrice
) {
}
