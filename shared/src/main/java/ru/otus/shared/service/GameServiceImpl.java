package ru.otus.shared.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.shared.datasource.entity.GameEntity;
import ru.otus.shared.datasource.repository.ActiveGameRepository;
import ru.otus.shared.datasource.repository.GameRepository;

import java.time.Instant;
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
        UUID gameId = UUID.randomUUID();
        String uuid = gameId.toString();

        GameEntity gameEntity = new GameEntity();
        gameEntity.setIdGame(uuid);
        gameEntity.setCreatedDate(Instant.now());
        gameEntity.setActive(true);
        gameEntity.setCreated(Instant.now());
        gameEntity.setUpdated(Instant.now());
        gameRepository.saveAndFlush(gameEntity);

        activeGameRepository.insertActiveGameByGameIdAndUsername(gameId.toString(), users);

        return gameId.toString();
    }

    @Override
    public List<String> getUsersByGameId(String gameId) {
        return gameRepository.findAllUsersByGameId(gameId);
    }

}
