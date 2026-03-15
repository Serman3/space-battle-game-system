package ru.otus.game_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {

    private String accessToken;

    private String accessTokenExpiry;

    private String refreshToken;

    private String refreshTokenExpiry;
}
