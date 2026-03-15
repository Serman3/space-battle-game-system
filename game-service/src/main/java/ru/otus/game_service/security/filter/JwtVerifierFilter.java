package ru.otus.game_service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.game_service.service.GameService;
import ru.otus.shared.security.JwtAuthenticationConverter;
import ru.otus.shared.security.token.Token;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;

@Slf4j
public class JwtVerifierFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/v1/**", HttpMethod.POST.name());

    private JwtAuthenticationConverter jwtAuthenticationConverter;
    private GameService gameService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            Authentication authentication = jwtAuthenticationConverter.convert(request);
            if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                Token token = (Token) authentication.getPrincipal();
                String gameId = token.gameId();
                String username = token.subject();
                List<String> usersInGame = gameService.getUsersByGameId(gameId);
                log.info("GAME_ID: {}", gameId);
                log.info("USERNAME: {}", username);
                log.info("USERS_IN_GAME {}", usersInGame);
                if (token.expiresAt().isAfter(Instant.now()) && gameId != null && !gameId.isBlank() && usersInGame.contains(username)) {
                    PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(token, authentication.getCredentials(), token.authorities().stream().map(SimpleGrantedAuthority::new).toList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            throw new AccessDeniedException("User must be authenticated and is valid expires at date");
        }
        filterChain.doFilter(request, response);
    }

    public JwtVerifierFilter setJwtAuthenticationConverter(JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        return this;
    }

    public JwtVerifierFilter setGameService(GameService gameService) {
        this.gameService = gameService;
        return this;
    }

}
