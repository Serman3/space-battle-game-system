package ru.otus.game_service.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthorizationRequest {

    private String username;

    private String password;

    private String gameId;

}
