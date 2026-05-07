package com.example.distributed_database_project_server.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
class ErrorResponse {

    String error;
    Map<String, String> errors;

    ErrorResponse(String error) {
        this.error = error;
    }

    ErrorResponse(Map<String, String> errors) {
        this.errors = errors;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
