package io.github.blackfishlabs.forza.ui.exception;

public class ValidationError extends RuntimeException {

    private ValidationError(String message) {
        super(message);
    }

    public static ValidationError newError(String message) {
        return new ValidationError(message);
    }
}