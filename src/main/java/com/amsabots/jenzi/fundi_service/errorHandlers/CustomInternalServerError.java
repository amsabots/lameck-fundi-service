package com.amsabots.jenzi.fundi_service.errorHandlers;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
public class CustomInternalServerError extends RuntimeException{
    public CustomInternalServerError(String message) {
        super(message);
    }
}
