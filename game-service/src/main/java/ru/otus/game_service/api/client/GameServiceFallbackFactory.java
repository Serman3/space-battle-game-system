package ru.otus.game_service.api.client;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.otus.game_service.openapi.model.OrderRequestDto;
import ru.otus.game_service.openapi.model.OrderResponseDto;

@Profile("test")
@Component
public class GameServiceFallbackFactory implements FallbackFactory<GameServiceClient> {

    @Override
    public GameServiceClient create(Throwable cause) {
        return new GameServiceClient() {

            @Override
            public OrderResponseDto orderAction(String bearerToken, OrderRequestDto orderRequest) {
                return new OrderResponseDto();
            }
        };
    }

}
