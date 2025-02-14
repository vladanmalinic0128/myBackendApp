package com.userApp.backend.requests;

import java.util.List;

public record PostFitnessProgramRequest(
        Long id,
        String name,
        Double price,
        Long categoryId,
        Long locationId,
        Integer weightLevel,
        Integer duration,
        String description,
        String link,
        List<SpecialAttributeRequest> specialAttributes,
        List<ImageRequest> images
) {
}
