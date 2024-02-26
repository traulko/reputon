package com.traulko.reputon.exception;

public abstract class BasicApiException extends RuntimeException {
    public BasicApiException(String message) {
        super(message);
    }
}
