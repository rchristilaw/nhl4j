package com.nhl4j.exception;

public class NhlApiException extends Exception {
    public NhlApiException (String message) {
        super(message);
    }

    public NhlApiException(String message, Exception ex) {
        super(message, ex);
    }
}
