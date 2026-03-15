package ru.otus.game_service.game.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.shared.annotation.Id;
import ru.otus.shared.command.Command;
import ru.otus.shared.model.UObject;

import java.util.Map;

@Id("StartShot")
@Component
@Scope("prototype")
public class ShotCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShotCommand.class);

    private final UObject gameObject;
    private final Map<String, Object> args;

    public ShotCommand(UObject gameObject,
                       Map<String, Object> args) {
        this.gameObject = gameObject;
        this.args = args;
    }

    @Override
    public void execute() {
        Integer shotToPoint = (Integer) args.get("shotToPoint");
        gameObject.setProperty("shotToPoint", shotToPoint);
        LOGGER.info("Shouting object {} with shot {}", gameObject.getId(), shotToPoint);
    }
}