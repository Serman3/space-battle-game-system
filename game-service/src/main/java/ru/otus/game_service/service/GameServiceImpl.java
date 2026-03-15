package ru.otus.game_service.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.game_service.datasource.entity.ActiveGameEntity;
import ru.otus.game_service.datasource.entity.GameEntity;
import ru.otus.game_service.datasource.entity.UserEntity;
import ru.otus.game_service.datasource.repository.ActiveGameRepository;
import ru.otus.game_service.datasource.repository.GameRepository;
import ru.otus.game_service.datasource.repository.UserRepository;
import ru.otus.game_service.ex.GameProcessingException;
import ru.otus.game_service.saga.events.GameApprovedEvent;
import ru.otus.game_service.saga.events.GameCreatedEvent;
import ru.otus.game_service.saga.events.GameRejectedEvent;
import ru.otus.game_service.utils.GameStatus;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameServiceImpl implements GameService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ActiveGameRepository activeGameRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String gameEventTopicName;

    @Autowired
    public GameServiceImpl(Environment environment,
                           UserRepository userRepository,
                           GameRepository gameRepository,
                           ActiveGameRepository activeGameRepository,
                           KafkaTemplate<String, Object> kafkaTemplate) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.activeGameRepository = activeGameRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.gameEventTopicName = environment.getProperty("kafka.topics.games.events.topic.name");
    }

    @Override
    @Transactional
    public String createGame(UUID gameId, List<String> users) {
        String uuid = gameId.toString();

        GameEntity gameEntity = new GameEntity();
        gameEntity.setIdGame(uuid);
        gameEntity.setActive(true);
        gameEntity.setCreated(Instant.now());
        gameEntity.setUpdated(Instant.now());
        gameEntity.setStatus(GameStatus.CREATED);
        gameRepository.saveAndFlush(gameEntity);

        List<ActiveGameEntity> activeGameEntityList = users
                .stream()
                .map(username -> {
                    Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
                    if (userEntityOptional.isPresent()) {
                        ActiveGameEntity activeGameEntity = new ActiveGameEntity();
                        activeGameEntity.setGame(gameEntity);
                        activeGameEntity.setUser(userEntityOptional.get());
                        activeGameEntity.setActive(true);
                        activeGameEntity.setCreated(Instant.now());
                        activeGameEntity.setUpdated(Instant.now());
                        return activeGameEntity;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        if (users.isEmpty()) {
            kafkaTemplate.send(new ProducerRecord<>(gameEventTopicName, new GameRejectedEvent(gameId)));
            return null;
        } else {
            activeGameRepository.saveAll(activeGameEntityList);

            kafkaTemplate.send(new ProducerRecord<>(gameEventTopicName, new GameCreatedEvent(gameId, users)));
            return gameId.toString();
        }
    }

    @Override
    @Transactional
    public void approveGame(UUID gameId) {
        gameRepository.findByIdGame(gameId.toString())
                .ifPresentOrElse(gameEntity -> {
                    gameEntity.setStatus(GameStatus.APPROVED);
                    gameRepository.save(gameEntity);
                    kafkaTemplate.send(new ProducerRecord<>(gameEventTopicName, new GameApprovedEvent(gameId)));
                }, () -> {
                    throw new GameProcessingException("Game not found by id: " + gameId);
                });
    }

    @Override
    @Transactional
    public void rejectGame(UUID gameId) {
        gameRepository.findByIdGame(gameId.toString())
                .ifPresentOrElse(gameEntity -> {
                    gameEntity.setStatus(GameStatus.REJECTED);
                    gameRepository.save(gameEntity);
                }, () -> {
                    throw new GameProcessingException("Game not found by id: " + gameId);
                });
    }

    @Override
    public List<String> getUsersByGameId(String gameId) {
        return gameRepository.findAllUsersByGameId(gameId);
    }

}
