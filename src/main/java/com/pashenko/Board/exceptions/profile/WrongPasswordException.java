package com.pashenko.Board.exceptions.profile;


public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String message) {
        super(message);
    }

    public WrongPasswordException() {
        super("Wrong password!");
    }
}
