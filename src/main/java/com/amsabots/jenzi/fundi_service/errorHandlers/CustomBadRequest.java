package com.amsabots.jenzi.fundi_service.errorHandlers;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
public class CustomBadRequest extends RuntimeException{
    public CustomBadRequest(String message) {
        super(message);
    }
}
