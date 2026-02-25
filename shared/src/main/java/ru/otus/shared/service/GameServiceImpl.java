package ru.otus.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.shared.datasource.entity.GameEntity;
import ru.otus.shared.datasource.repository.ActiveGameRepository;
import ru.otus.shared.datasource.repository.GameRepository;

import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final ActiveGameRepository activeGameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository,
                           ActiveGameRepository activeGameRepository) {
        this.gameRepository = gameRepository;
        this.activeGameRepository = activeGameRepository;
    }

    @Override
    @Transactional
    public String createGame(List<String> users) {
        String uuid = UUID.randomUUID().toString();

        GameEntity gameEntity = new GameEntity();
        gameEntity.setIdGame(uuid);
        gameRepository.save(gameEntity);

        users.forEach(user -> {
            activeGameRepository.insertActiveGameByGameIdAndUsername(uuid, user);
        });

        return uuid;
    }

    @Override
    public List<String> getUsersByGameId(String gameId) {
        return gameRepository.findAllUsersByGameId(gameId);
    }

}
