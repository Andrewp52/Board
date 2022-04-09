package com.pashenko.Board.exceptions.registration;

public class EmailOccupiedExceprtion extends RegistrationException {
    private String message;
    public EmailOccupiedExceprtion(String message) {
        super(message);
    }

    public EmailOccupiedExceprtion() {
        super("Email is already exist");
    }
}
