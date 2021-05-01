package ru.otus.istyazhkina.library.exceptions;

public class NoEntityFoundInDataBaseException extends RuntimeException {

    public NoEntityFoundInDataBaseException() {
    }

    public NoEntityFoundInDataBaseException(String message) {
        super(message);
    }

    public NoEntityFoundInDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
