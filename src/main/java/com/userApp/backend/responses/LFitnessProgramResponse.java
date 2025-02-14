package com.userApp.backend.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LFitnessProgramResponse {
    Long id;
    String name;
    Double price;
    String createdAt;
    List<LImageResponse> images;
    String categoryName;
    String locationName;
    Integer weightLevel;
    String duration;
    String fullName;
    String email;
    List<LSpecialAttributesResponse> special_attrs;
    String description;
    String link;
}
