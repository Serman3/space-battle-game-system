package ru.otus.game_service.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.otus.game_service.security.filter.JwtVerifierFilter;
import ru.otus.game_service.security.filter.RequestGameJwtTokensFilter;
import ru.otus.game_service.service.GameService;
import ru.otus.shared.security.JwtAuthenticationConverter;
import ru.otus.shared.security.token.Token;

import java.util.function.Function;

public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> refreshTokenStringSerializer = Object::toString;

    private Function<Token, String> accessTokenStringSerializer = Object::toString;

    private Function<String, Token> accessTokenStringDeserializer;

    private Function<String, Token> refreshTokenStringDeserializer;

    private GameService gameService;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        var requestGameJwtTokensFilter = new RequestGameJwtTokensFilter();
        requestGameJwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        requestGameJwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);
        requestGameJwtTokensFilter.setGameService(gameService);

        var jwtVerifierFilter = new JwtVerifierFilter()
                .setJwtAuthenticationConverter(new JwtAuthenticationConverter(this.accessTokenStringDeserializer, this.refreshTokenStringDeserializer));

        builder
                .addFilterBefore(requestGameJwtTokensFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtVerifierFilter, RequestGameJwtTokensFilter.class);
    }

    public JwtAuthenticationConfigurer accessTokenStringDeserializer(
            Function<String, Token> accessTokenStringDeserializer) {
        this.accessTokenStringDeserializer = accessTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenStringDeserializer(
            Function<String, Token> refreshTokenStringDeserializer) {
        this.refreshTokenStringDeserializer = refreshTokenStringDeserializer;
        return this;
    }

    public JwtAuthenticationConfigurer refreshTokenStringSerializer(
            Function<Token, String> refreshTokenStringSerializer) {
        this.refreshTokenStringSerializer = refreshTokenStringSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer accessTokenStringSerializer(
            Function<Token, String> accessTokenStringSerializer) {
        this.accessTokenStringSerializer = accessTokenStringSerializer;
        return this;
    }

    public JwtAuthenticationConfigurer gameService(GameService service) {
        this.gameService = service;
        return this;
    }
}

