package ru.otus.storage;

import ru.otus.command.Command;
import ru.otus.model.UObject;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface GameContext {

    String getGameId();

    List<String> getUsers();

    BlockingQueue<Command> getCommandQueue(String userId);

    void addCommand(String userId, Command command);

    UObject getGameObject(String userId, String objectId);

}
