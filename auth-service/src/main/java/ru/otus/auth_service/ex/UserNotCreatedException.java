package ru.otus.auth_service.ex;

public class UserNotCreatedException extends RuntimeException{

    public UserNotCreatedException(String message){
        super(message);
    }
}
