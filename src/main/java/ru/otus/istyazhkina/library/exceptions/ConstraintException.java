package ru.otus.istyazhkina.library.exceptions;

public class ConstraintException extends RuntimeException {

    public ConstraintException() {
    }

    public ConstraintException(String message) {
        super(message);
    }

    public ConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
