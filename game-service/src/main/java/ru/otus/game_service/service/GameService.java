package ru.otus.game_service.service;

import java.util.List;
import java.util.UUID;

public interface GameService {

    String createGame(UUID gameId, List<String> users);

    void approveGame(UUID gameId);

    void rejectGame(UUID gameId);

    List<String> getUsersByGameId(String gameId);
}
