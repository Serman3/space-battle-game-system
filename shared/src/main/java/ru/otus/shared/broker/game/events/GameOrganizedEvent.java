package ru.otus.shared.broker.game.events;

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
public class GameOrganizedEvent {

    private UUID gameId;
    private List<String> users;
}
