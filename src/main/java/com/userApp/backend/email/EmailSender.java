package com.userApp.backend.email;

public interface EmailSender {
    void send(String to, String email);
}
