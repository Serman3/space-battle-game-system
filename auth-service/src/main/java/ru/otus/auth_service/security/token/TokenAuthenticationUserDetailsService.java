package ru.otus.auth_service.security.token;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.otus.auth_service.service.UserAuthService;
import ru.otus.security.token.Token;

import java.time.Instant;

public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserAuthService userAuthService;

    public TokenAuthenticationUserDetailsService(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(
                    token.subject(),
                    userAuthService.findUserByUsername(token.subject()).get().getPassword(),
                    true,
                    true,
                    !userAuthService.existsDeactivateToken(token) && token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.authorities().stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList(), token
            );
        }

        throw new UsernameNotFoundException("Principal must me of type Token");
    }
}
