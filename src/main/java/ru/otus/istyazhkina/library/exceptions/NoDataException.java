package ru.otus.istyazhkina.library.exceptions;

public class NoDataException extends RuntimeException {

    public NoDataException() {
    }

    public NoDataException(String message) {
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
