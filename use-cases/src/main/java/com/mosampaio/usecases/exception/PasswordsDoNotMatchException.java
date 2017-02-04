package com.mosampaio.usecases.exception;

public class PasswordsDoNotMatchException extends UseCaseException {

    public PasswordsDoNotMatchException() {
        super("Passwords do not match.");
    }
}
