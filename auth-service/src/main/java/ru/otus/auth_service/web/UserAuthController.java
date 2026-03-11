package ru.otus.auth_service.web;

import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.auth_service.api.client.JwtClient;
import ru.otus.auth_service.datasource.dto.UserDto;
import ru.otus.auth_service.ex.UserNotCreatedException;
import ru.otus.auth_service.openapi.api.AuthApi;
import ru.otus.auth_service.openapi.model.*;
import ru.otus.auth_service.service.UserAuthService;
import ru.otus.auth_service.validation.UserRegistrationValidator;
import ru.otus.shared.util.BasicAuthUtil;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserAuthController implements AuthApi {

    private final JwtClient jwtClient;
    private final UserAuthService userAuthService;
    private final UserRegistrationValidator userRegistrationValidator;

    @Override
    public ResponseEntity<JwtAuthorizationResponseDto> authorize(JwtAuthorizationRequestDto jwtAuthorizationRequestDto) {
        return ResponseEntity.of(Optional.of(jwtClient.performToken(
                "Basic " + BasicAuthUtil.encodeToBase64(jwtAuthorizationRequestDto.getUsername() + ":" + jwtAuthorizationRequestDto.getPassword()),
                jwtAuthorizationRequestDto.getGameId()
        )));
    }

    @Override
    public ResponseEntity<Void> compensateRegistration(String id) {
        throw new RuntimeException("Метод не поддерживается");
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
    public ResponseEntity<String> organizeSpacebattle(OrganaizeSpaceBattleRequestDto organaizeSpaceBattleRequestDto) {
       // return ResponseEntity.ok(gameService.createGame(organaizeSpaceBattleRequestDto.getUsers()));
        return ResponseEntity.ok("");
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(UserNotCreatedException exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(Throwable exception) {
        log.error("ErrorMessage", exception);
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
