package com.userApp.backend.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatUsersResponse {
    private List<UserChatResponse> advisors;
    private List<UserChatResponse> users;
}
