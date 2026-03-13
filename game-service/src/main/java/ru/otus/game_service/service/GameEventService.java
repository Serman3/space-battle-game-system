package ru.otus.game_service.service;

import ru.otus.game_service.utils.GameStatus;

import java.util.UUID;

public interface GameEventService {

    void addEvent(UUID gameId, GameStatus gameStatus);
}
