package ru.otus.game_service.saga.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class GameCreatedEvent {

    private UUID gameId;
    private List<String> users;
}
