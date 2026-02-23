package ru.otus.game_service.ex;

public class OrderParseException extends RuntimeException {

    public OrderParseException(String message){
        super(message);
    }
}
