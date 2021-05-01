package ru.otus.istyazhkina.library.exceptions;

public class SameEntityAlreadyExistsException extends RuntimeException {

    public SameEntityAlreadyExistsException() {
    }

    public SameEntityAlreadyExistsException(String message) {
        super(message);
    }

    public SameEntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
