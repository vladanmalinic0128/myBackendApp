package com.userApp.backend.responses;

import lombok.Data;

@Data
public class SubscriptionResponse {
    private Long categoryId;
    private String categoryName;
    private String specialAttributes;
    private boolean exists;
}
