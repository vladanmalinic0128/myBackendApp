package com.userApp.backend.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {
    String fullName;
    String avatar;
    String time;
    String content;
    Boolean response;
}
