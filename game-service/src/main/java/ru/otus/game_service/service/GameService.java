package ru.otus.game_service.service;

import java.util.List;

public interface GameService {

    String createGame(List<String> users);

    List<String> getUsersByGameId(String gameId);
}
