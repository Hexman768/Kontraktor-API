package com.pedigo.Kontraktor.exception;

public class FailedToInsertException extends Exception {
    public FailedToInsertException() {
        super("Failed to insert value into database!");
    }

    public FailedToInsertException(String message) {
        super(message);
    }
}
