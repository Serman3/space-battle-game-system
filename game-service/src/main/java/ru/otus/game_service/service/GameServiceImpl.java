package ru.otus.game_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.game_service.datasource.entity.ActiveGameEntity;
import ru.otus.game_service.datasource.entity.GameEntity;
import ru.otus.game_service.datasource.repository.ActiveGameRepository;
import ru.otus.game_service.datasource.repository.GameRepository;
import ru.otus.game_service.datasource.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ActiveGameRepository activeGameRepository;

    @Autowired
    public GameServiceImpl(UserRepository userRepository,
                           GameRepository gameRepository,
                           ActiveGameRepository activeGameRepository) {
        this.userRepository = userRepository;
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
        gameEntity.setActive(true);
        gameEntity.setCreated(Instant.now());
        gameEntity.setUpdated(Instant.now());
        gameRepository.saveAndFlush(gameEntity);

        List<ActiveGameEntity> activeGameEntityList = users
                .stream()
                .filter(username -> userRepository.findByUsername(username).isPresent())
                .map(username -> {
                    ActiveGameEntity activeGameEntity = new ActiveGameEntity();
                    activeGameEntity.setGame(gameEntity);
                    activeGameEntity.setUser(userRepository.findByUsername(username).get());
                    activeGameEntity.setActive(true);
                    activeGameEntity.setCreated(Instant.now());
                    activeGameEntity.setUpdated(Instant.now());
                    return activeGameEntity;
                })
                .toList();

        activeGameRepository.saveAll(activeGameEntityList);

        return gameId.toString();
    }

    @Override
    public List<String> getUsersByGameId(String gameId) {
        return gameRepository.findAllUsersByGameId(gameId);
    }

}
