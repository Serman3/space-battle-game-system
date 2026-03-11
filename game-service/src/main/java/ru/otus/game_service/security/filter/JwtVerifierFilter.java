package ru.otus.game_service.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.otus.shared.security.JwtAuthenticationConverter;
import ru.otus.shared.security.token.Token;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;

public class JwtVerifierFilter extends OncePerRequestFilter {

    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/v1/**", HttpMethod.POST.name());

    private JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            Authentication authentication = jwtAuthenticationConverter.convert(request);
            if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                Token token = (Token) authentication.getPrincipal();
                if (token.expiresAt().isAfter(Instant.now())) {
                    PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(token, authentication.getCredentials(), token.authorities().stream().map(SimpleGrantedAuthority::new).toList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
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

}
