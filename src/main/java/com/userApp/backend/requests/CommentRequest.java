package com.userApp.backend.requests;

public record CommentRequest(
        String content,
        Long fitnessProgramId
) {
}
