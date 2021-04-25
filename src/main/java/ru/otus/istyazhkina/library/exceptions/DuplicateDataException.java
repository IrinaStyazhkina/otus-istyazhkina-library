package ru.otus.istyazhkina.library.exceptions;

public class DuplicateDataException extends RuntimeException {

    public DuplicateDataException() {
    }

    public DuplicateDataException(String message) {
        super(message);
    }

    public DuplicateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
