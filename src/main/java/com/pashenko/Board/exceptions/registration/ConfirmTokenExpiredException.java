package com.pashenko.Board.exceptions.registration;

public class ConfirmTokenExpiredException extends RegistrationException{
    public ConfirmTokenExpiredException(String message) {
        super(message);
    }

    public ConfirmTokenExpiredException() {
        super("Confirmation token expired!");
    }
}
