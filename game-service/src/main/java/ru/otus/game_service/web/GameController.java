package ru.otus.game_service.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.game_service.model.Order;
import ru.otus.game_service.openapi.api.GameApi;
import ru.otus.game_service.openapi.model.OrderRequestDto;
import ru.otus.game_service.openapi.model.OrderResponseDto;
import ru.otus.game_service.service.GameOrderService;
import ru.otus.game_service.validator.OrderValidator;
import ru.otus.shared.security.token.Token;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Profile("prod")
public class GameController implements GameApi {

    private final OrderValidator orderValidator;
    private final ModelMapper modelMapper;
    private final GameOrderService gameOrderService;

    @Override
    public ResponseEntity<OrderResponseDto> order(OrderRequestDto orderRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Token token = (Token) authentication.getPrincipal();

        orderValidator.validate(token, orderRequestDto, null);

        Map<String, Object> gameObjectProperties = gameOrderService.orderAction(token.subject(), token.gameId(), modelMapper.map(orderRequestDto, Order.class));

        return ResponseEntity.ok(new OrderResponseDto(token.subject(), orderRequestDto.getActionId(), orderRequestDto.getGameObjectId(), gameObjectProperties));
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(Throwable exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
