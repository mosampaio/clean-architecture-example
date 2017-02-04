package com.mosampaio.usecases.exception;

public class UsernameAlreadyInUseException extends UseCaseException {

    public UsernameAlreadyInUseException() {
        super("That username is already in use.");
    }
}
