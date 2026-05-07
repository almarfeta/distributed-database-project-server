package com.example.distributed_database_project_server.authentication;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class AuthenticationResponse {

    private String message;
    private UUID userId;
    private String token;

    AuthenticationResponse(String message, UUID userId, String token) {
        this.message = message;
        this.userId = userId;
        this.token = token;
    }

    AuthenticationResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
