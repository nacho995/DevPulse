package com.devpulse.backend.infrastructure.adapter.in.web.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

