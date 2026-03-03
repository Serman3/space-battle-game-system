package ru.otus.game_service.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.otus.game_service.api.dto.*;

@Profile("test")
@FeignClient(qualifiers = "AuthClient", name = "AuthClient",  url = "${services.api-gateway-service.url}", configuration = ClientConfig.class, fallbackFactory = AuthFallbackFactory.class)
public interface AuthServiceClient {

    @PostMapping("/v1/auth/authorize")
    JwtAuthenticationResponse authorize(JwtAuthorizationRequest jwtAuthorizationRequest);

    @PostMapping("/v1/auth/register")
    Response register(RegistrationRequest registrationRequest);

    @PostMapping("/v1/auth/refresh")
    JwtAuthenticationResponse refresh(JwtTokenRequest jwtTokenRequest);

    @PostMapping("/v1/auth/logout")
    Response logout(JwtTokenRequest jwtTokenRequest);

    @PostMapping("/v1/auth/organaizeSpacebattle")
    String organaizeSpacebattle(@RequestHeader("Authorization") String accessToken, OrganaizeSpaceBattleRequest organaizeSpaceBattleRequest);

}
