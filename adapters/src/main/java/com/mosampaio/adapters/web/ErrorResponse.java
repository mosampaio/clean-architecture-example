package com.mosampaio.adapters.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

@EqualsAndHashCode
@ToString
public class ErrorResponse {
    @Getter
    private final List<String> errors;

    private ErrorResponse(List<String> messages) {
        errors = Collections.unmodifiableList(messages);
    }

    public static ErrorResponse errorMessage(String message) {
        return new ErrorResponse(asList(message));
    }

    public static ErrorResponse errorMessage(List<String> messages) {
        return new ErrorResponse(messages);
    }
}