package ru.otus.game_service.saga.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class GameApprovedEvent {

    private UUID gameId;
}
