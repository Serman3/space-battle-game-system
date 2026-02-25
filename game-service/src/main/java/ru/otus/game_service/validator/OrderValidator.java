package ru.otus.game_service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.otus.game_service.ex.OrderParseException;
import ru.otus.game_service.openapi.model.OrderRequestDto;
import ru.otus.shared.security.token.Token;
import ru.otus.shared.service.GameService;
import ru.otus.shared.validator.BaseValidator;

import java.util.List;

@Component
public class OrderValidator extends BaseValidator {

    private final GameService gameService;

    @Autowired
    public OrderValidator(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(OrderRequestDto.class);
    }

    public void validate(Token token, Object target, Errors errors) {
        validate(target, errors);
        List<String> usersInGame = gameService.getUsersByGameId(token.gameId());
        usersInGame.stream()
                .filter(u -> u.equals(token.subject()))
                .findFirst()
                .orElseThrow(() -> new OrderParseException("Игрок " + token.subject() + " не является участником игры " + token.gameId()));
    }

    @Override
    public void validate(Object target, Errors errors) {
        checkErrors(errors, OrderParseException::new);
    }
}
