package ru.otus.shared.storage;

import ru.otus.shared.command.Command;
import ru.otus.shared.model.UObject;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface GameContext {

    String getGameId();

    List<String> getUsers();

    BlockingQueue<Command> getCommandQueue(String userId);

    void addCommand(String userId, Command command);

    UObject getGameObject(String userId, String objectId);

}
