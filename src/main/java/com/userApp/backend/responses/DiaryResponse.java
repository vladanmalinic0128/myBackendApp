package com.userApp.backend.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DiaryResponse {
    private Double currentWeight;
    private String description;
    private Integer trainingDuration;
    private String createdAt;
}
