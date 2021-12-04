package com.amsabots.jenzi.fundi_service.errorHandlers;

public class CustomForbiddenResource extends RuntimeException{
    public CustomForbiddenResource(String message) {
        super(message);
    }
}
