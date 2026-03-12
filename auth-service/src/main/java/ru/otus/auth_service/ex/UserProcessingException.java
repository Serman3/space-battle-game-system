package ru.otus.auth_service.ex;

public class UserProcessingException extends RuntimeException {

    public UserProcessingException(String message) {
        super(message);
    }
}
