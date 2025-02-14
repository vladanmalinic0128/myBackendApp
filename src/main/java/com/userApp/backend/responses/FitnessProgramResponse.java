package com.userApp.backend.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FitnessProgramResponse {
    Long id;
    String name;
    Double price;
    Integer weightLevel;
    String categoryName;
    String locationName;
    String image;
    String link;
}
