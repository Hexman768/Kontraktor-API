package com.pedigo.Kontraktor.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String id) {
        super("User with id: " + id + " does not exist!");
    }
}
