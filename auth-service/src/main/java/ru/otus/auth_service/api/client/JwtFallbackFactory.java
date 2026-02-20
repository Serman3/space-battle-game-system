package ru.otus.auth_service.api.client;

import feign.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.otus.auth_service.openapi.model.JwtAuthorizationResponseDto;

@Component
public class JwtFallbackFactory implements FallbackFactory<JwtClient> {

    @Override
    public JwtClient create(Throwable cause) {
        return new JwtClient() {
            @Override
            public JwtAuthorizationResponseDto performToken(String basic, String gameId) {
                return null;
            }

            @Override
            public JwtAuthorizationResponseDto refreshToken(String refreshToken) {
                return null;
            }

            @Override
            public Response logOut(String accessToken) {
                return null;
            }
        };
    }
}
