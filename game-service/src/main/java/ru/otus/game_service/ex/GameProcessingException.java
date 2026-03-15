package ru.otus.game_service.ex;

public class GameProcessingException extends RuntimeException {

    public GameProcessingException(String message) {
        super(message);
    }
}
