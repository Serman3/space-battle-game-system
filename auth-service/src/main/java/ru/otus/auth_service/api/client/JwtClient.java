package ru.otus.auth_service.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.auth_service.openapi.model.JwtAuthorizationResponseDto;

@FeignClient(qualifiers = "JwtClient", name = "auth-service", configuration = JwtClientConfig.class, fallbackFactory = JwtFallbackFactory.class)
public interface JwtClient {

    @PostMapping("/jwt/tokens")
    JwtAuthorizationResponseDto performToken(@RequestHeader("Authorization") String basic, @RequestHeader(value = "gameId", required = false) String gameId);

    @PostMapping("/jwt/refresh")
    JwtAuthorizationResponseDto refreshToken(@RequestHeader("Authorization") String refreshToken);

    @PostMapping("/jwt/logout")
    Response logOut(@RequestHeader("Authorization") String refreshToken);

}
