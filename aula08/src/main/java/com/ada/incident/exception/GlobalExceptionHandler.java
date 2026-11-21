package com.ada.incident.exception;

import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistException.class)
    public  ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        logger.error("UserAlreadyExistException: {}", ex.getMessage());
        return responseEntity(HttpStatus.CONFLICT, ex.getMessage());

    }

    @ExceptionHandler(IncidentNotFoundException.class)
    public  ResponseEntity<ErrorResponse> handleIncidentNotFoundException(IncidentNotFoundException ex) {
        logger.error("IncidentNotFoundException: {}", ex.getMessage());
        return responseEntity(HttpStatus.NOT_FOUND, ex.getMessage());

    }

    @ExceptionHandler(StatusChangeException.class)
    public  ResponseEntity<ErrorResponse> handleStatusChangeException(StatusChangeException ex) {
        logger.error("StatusChangeException: {}", ex.getMessage());
        return responseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());

    }

    @ExceptionHandler(AuthenticationException.class)
    public  ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException  ex) {
        logger.error("UsernameNotFoundException: {}", ex.getMessage());
        return responseEntity(HttpStatus.UNAUTHORIZED, "Invalid credentials");

    }

    @ExceptionHandler(Exception.class)
    public  ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        return responseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }

    private ResponseEntity<ErrorResponse> responseEntity(HttpStatus status, String mensagem) {
        ErrorResponse corpo = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                Instant.now()
        );
        return ResponseEntity.status(status).body(corpo);
    }
}
