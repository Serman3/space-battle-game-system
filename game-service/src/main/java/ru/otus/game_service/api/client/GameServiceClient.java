package ru.otus.game_service.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.game_service.openapi.model.OrderRequestDto;
import ru.otus.game_service.openapi.model.OrderResponseDto;

@Profile("test")
@FeignClient(qualifiers = "GameClient", name = "GameClient", url = "${services.api-gateway-service.url}", configuration = ClientConfig.class, fallbackFactory = AuthFallbackFactory.class)
public interface GameServiceClient {

    @PostMapping("/v1/game/order")
    OrderResponseDto orderAction(@RequestHeader("Authorization") String bearerToken, OrderRequestDto orderRequest);
}
