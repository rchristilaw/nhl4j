package com.nhl4j.exception;

public class StatsApiException extends Exception {
    public StatsApiException(String message) {
        super(message);
    }

    public StatsApiException(String message, Exception ex) {
        super(message, ex);
    }
}
