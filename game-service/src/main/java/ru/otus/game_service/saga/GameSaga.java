package ru.otus.game_service.saga;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.game_service.service.GameEventService;
import ru.otus.game_service.saga.commands.ApproveGameCommand;
import ru.otus.game_service.saga.commands.CreateGameCommand;
import ru.otus.game_service.saga.commands.RejectGameCommand;
import ru.otus.game_service.saga.events.GameApprovedEvent;
import ru.otus.game_service.saga.events.GameCreatedEvent;
import ru.otus.shared.broker.game.events.GameOrganizedEvent;
import ru.otus.game_service.saga.events.GameRejectedEvent;
import ru.otus.game_service.utils.GameStatus;

@Slf4j
@KafkaListener(topics = {
        "${kafka.topics.games.events.topic.name}"
})
@Component
public class GameSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final GameEventService gameEventService;
    private final String gameCommandTopicName;

    @Autowired
    public GameSaga(Environment environment,
                    KafkaTemplate<String, Object> kafkaTemplate,
                    GameEventService gameEventService) {
        this.kafkaTemplate = kafkaTemplate;
        this.gameCommandTopicName = environment.getProperty("kafka.topics.games.commands.topic.name");
        this.gameEventService = gameEventService;
    }

    @KafkaHandler
    public void handleEvent(@Payload GameOrganizedEvent event) {
        log.info("Received message for game organize event: {}", event);
        CreateGameCommand createGameCommand = new CreateGameCommand(event.getGameId(), event.getUsers());

        kafkaTemplate.send(gameCommandTopicName, createGameCommand);
        gameEventService.addEvent(event.getGameId(), GameStatus.ORGANIZE);
    }

    @KafkaHandler
    public void handleEvent(@Payload GameCreatedEvent event) {
        log.info("Received message for game created event: {}", event);
        ApproveGameCommand approveGameCommand = new ApproveGameCommand(event.getGameId());

        kafkaTemplate.send(gameCommandTopicName, approveGameCommand);
        gameEventService.addEvent(event.getGameId(), GameStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(@Payload GameApprovedEvent event) {
        log.info("Received message for game approved event: {}", event);
        gameEventService.addEvent(event.getGameId(), GameStatus.APPROVED);
    }

    @KafkaHandler
    public void handleEvent(@Payload GameRejectedEvent event) {
        log.info("Received message for game rejected event: {}", event);
        RejectGameCommand rejectGameCommand = new RejectGameCommand(event.getGameId());

        kafkaTemplate.send(gameCommandTopicName, rejectGameCommand);
        gameEventService.addEvent(event.getGameId(), GameStatus.REJECTED);
    }
}
