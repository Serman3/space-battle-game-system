package ru.otus.game_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.game_service.datasource.entity.GameEventEntity;
import ru.otus.game_service.datasource.repository.GameEventRepository;
import ru.otus.game_service.utils.GameStatus;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameEventServiceImpl implements GameEventService {

    private final GameEventRepository gameEventRepository;

    @Override
    @Transactional
    public void addEvent(UUID gameId, GameStatus gameStatus) {
        GameEventEntity gameEventEntity = new GameEventEntity();
        gameEventEntity.setActive(true);
        gameEventEntity.setCreated(Instant.now());
        gameEventEntity.setUpdated(Instant.now());
        gameEventEntity.setGameId(gameId);
        gameEventEntity.setStatus(gameStatus);

        gameEventRepository.save(gameEventEntity);
    }
}
