package com.userApp.backend.requests;

public record EditDataRequest(
     String firstname,
     String lastname,
     Long cityId,
     String image
) {
}
