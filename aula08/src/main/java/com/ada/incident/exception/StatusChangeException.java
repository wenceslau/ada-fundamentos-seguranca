package com.ada.incident.exception;

public class StatusChangeException extends RuntimeException {

    public StatusChangeException(String status) {
        super("Cannot change incident status to '" + status + "'.");
    }
}
