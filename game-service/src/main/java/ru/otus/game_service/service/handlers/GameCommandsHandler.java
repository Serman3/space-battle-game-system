package ru.otus.game_service.service.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.game_service.service.GameService;
import ru.otus.game_service.saga.commands.CreateGameCommand;
import ru.otus.game_service.saga.commands.ApproveGameCommand;
import ru.otus.game_service.saga.commands.RejectGameCommand;

@Slf4j
@KafkaListener(topics = {
        "${kafka.topics.games.commands.topic.name}"
})
@Component
@RequiredArgsConstructor
public class GameCommandsHandler {

    private final GameService gameService;

    @KafkaHandler
    public void handleEvent(@Payload CreateGameCommand command) {
        log.info("Received message for game created command: {}", command);
        gameService.createGame(command.getGameId(), command.getUsers());
    }

    @KafkaHandler
    public void handleEvent(@Payload ApproveGameCommand command) {
        log.info("Received message for game approve command: {}", command);
        gameService.approveGame(command.getGameId());
    }

    @KafkaHandler
    public void handleEvent(@Payload RejectGameCommand command) {
        log.info("Received message for game reject command: {}", command);
        gameService.rejectGame(command.getGameId());
    }
}
