package ru.otus.istyazhkina.library.exceptions;

public class ProhibitedDeletionException extends RuntimeException {

    public ProhibitedDeletionException() {
    }

    public ProhibitedDeletionException(String message) {
        super(message);
    }

    public ProhibitedDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
