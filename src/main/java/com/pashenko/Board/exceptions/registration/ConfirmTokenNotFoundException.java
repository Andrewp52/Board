package com.pashenko.Board.exceptions.registration;

public class ConfirmTokenNotFoundException extends RegistrationException{
    public ConfirmTokenNotFoundException(String message) {
        super(message);
    }

    public ConfirmTokenNotFoundException() {
        super("Confirmation token not found!");
    }
}
