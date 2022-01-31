package com.amsabots.jenzi.fundi_service.errorHandlers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.persistence.RollbackException;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
@ControllerAdvice
public class ErrorAdvisorHandler {

    @ExceptionHandler({CustomBadRequest.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorEntity> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorEntity(ex.getMessage(), HttpStatus.BAD_REQUEST.value(), "ERRBADREQUEST"));
    }

    @ExceptionHandler(CustomInternalServerError.class)
    public ResponseEntity<ErrorEntity> handleInterServerError(Exception ex) {
        return ResponseEntity.internalServerError().body(new ErrorEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "SERVERERR"));
    }

    @ExceptionHandler(CustomResourceNotFound.class)
    public ResponseEntity<ErrorEntity> handleResourceNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorEntity(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "NOT_FOUND"));
    }

    @ExceptionHandler({RollbackException.class,
            EmptyResultDataAccessException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ErrorEntity> handleJpaSqlException(Exception ex) {
        if (ex.getMessage().contains("exists"))
            return ResponseEntity.unprocessableEntity().body(new ErrorEntity("Resource with the id provided cannot be found.",
                    HttpStatus.UNPROCESSABLE_ENTITY.value(), "SQL_EXCEPTION"));
        return ResponseEntity.unprocessableEntity().body(new ErrorEntity(ex.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(), "SQL_EXCEPTION"));
    }

    @ExceptionHandler({WebClientException.class, WebClientRequestException.class, WebClientResponseException.class})
    public ResponseEntity<ErrorEntity> handleMissingParameters(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorEntity(ex.getMessage(), HttpStatus.NOT_FOUND.value(), "NOT_FOUND"));
    }

    @ExceptionHandler(CustomForbiddenResource.class)
    public ResponseEntity<ErrorEntity> handleForbiddenResource(Exception ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorEntity(ex.getMessage(), HttpStatus.FORBIDDEN.value(), "ERRBADREQUEST"));
    }
}
