package com.traulko.reputon.controller.handler;

import com.traulko.reputon.exception.ApiExceptionResponse;
import com.traulko.reputon.exception.ClientInternalException;
import com.traulko.reputon.exception.ServerInternalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ServerInternalException.class})
    public ResponseEntity<ApiExceptionResponse> handleApiException(ServerInternalException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createApiError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(value = {ClientInternalException.class})
    public ResponseEntity<ApiExceptionResponse> handleApiException(ClientInternalException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createApiError(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    private ApiExceptionResponse createApiError(String exceptionMessage, HttpStatus status) {
        return new ApiExceptionResponse(exceptionMessage,
                status,
                LocalDateTime.now());
    }
}
