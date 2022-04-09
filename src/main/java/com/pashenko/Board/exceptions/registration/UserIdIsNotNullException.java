package com.pashenko.Board.exceptions.registration;

public class UserIdIsNotNullException extends RegistrationException {
    public UserIdIsNotNullException(String message) {
        super(message);
    }

    public UserIdIsNotNullException() {
        super("User id must be null");
    }
}
