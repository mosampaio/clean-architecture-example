package com.mosampaio.usecases.exception;

public class IncorrectUsernameOrPasswordException extends UseCaseException {

    public IncorrectUsernameOrPasswordException() {
        super("Incorrect Username or Password");
    }
}
