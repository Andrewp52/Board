package com.pashenko.Board.exceptions.registration;

public class RegistrationException extends RuntimeException{
    public RegistrationException(String message) {
        super("Registration failed: " + message);
    }
}
