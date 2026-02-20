package ru.otus.auth_service.web;

import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.auth_service.api.client.JwtClient;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.ex.UserNotCreatedException;
import ru.otus.auth_service.openapi.api.V1Api;
import ru.otus.auth_service.openapi.model.*;
import ru.otus.auth_service.service.UserAuthService;
import ru.otus.auth_service.validation.UserRegistrationValidator;
import ru.otus.service.GameService;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserAuthController implements V1Api {

    private final JwtClient jwtClient;
    private final GameService gameService;
    private final UserAuthService userAuthService;
    private final UserRegistrationValidator userRegistrationValidator;

    @Override
    public ResponseEntity<JwtAuthorizationResponseDto> authorize(JwtAuthorizationRequestDto jwtAuthorizationRequestDto) {
        return ResponseEntity.of(Optional.of(jwtClient.performToken(
                "Basic " + encodeToBase64(jwtAuthorizationRequestDto.getUsername() + ":" + jwtAuthorizationRequestDto.getPassword()),
                jwtAuthorizationRequestDto.getGameId().toString()
        )));
    }

    @Override
    public ResponseEntity<String> register(RegistrationRequestDto registrationRequestDto) {
        userRegistrationValidator.validate(registrationRequestDto, null);
        userAuthService.addUser(new UserDto(registrationRequestDto.getUsername(), registrationRequestDto.getPassword()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<JwtAuthorizationResponseDto> refresh(JwtTokenRequestDto jwtTokenRequestDto) {
        return ResponseEntity.of(Optional.of(jwtClient.refreshToken("Bearer " + jwtTokenRequestDto.getToken())));
    }

    @Override
    public ResponseEntity<Void> logout(JwtTokenRequestDto jwtTokenRequestDto) {
        try (Response response = jwtClient.logOut("Bearer " + jwtTokenRequestDto.getToken())) {
            if (response != null && response.status() == HttpStatus.NO_CONTENT.value())
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<UUID> organizeSpacebattle(OrganaizeSpaceBattleRequestDto organaizeSpaceBattleRequestDto) {
        return ResponseEntity.ok(UUID.fromString(gameService.createGame(organaizeSpaceBattleRequestDto.getUsers())));
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(UserNotCreatedException exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(Throwable exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private String encodeToBase64(String credentials) {
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

}
