package ru.otus.game_service.service.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.otus.game_service.service.GameService;
import ru.otus.game_service.saga.commands.CreateGameCommand;
import ru.otus.game_service.saga.commands.ApproveGameCommand;
import ru.otus.game_service.saga.commands.RejectGameCommand;

@KafkaListener(topics = {
        "${kafka.topics.games.commands.topic.name}"
})
@Component
@RequiredArgsConstructor
public class GameCommandsHandler {

    private final GameService gameService;

    @KafkaHandler
    public void handleEvent(@Payload CreateGameCommand command) {
        gameService.createGame(command.getGameId(), command.getUsers());
    }

    @KafkaHandler
    public void handleEvent(@Payload ApproveGameCommand command) {
        gameService.approveGame(command.getGameId());
    }

    @KafkaHandler
    public void handleEvent(@Payload RejectGameCommand command) {
        gameService.rejectGame(command.getGameId());
    }
}
