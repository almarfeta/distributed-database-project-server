package com.example.distributed_database_project_server.authentication;

import static com.example.distributed_database_project_server.authentication.AuthenticationConstants.AUTHORIZATION_HEADER;
import static com.example.distributed_database_project_server.authentication.AuthenticationConstants.BEARER_TOKEN_PREFIX;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.entity.TokenEntity;
import com.example.distributed_database_project_server.domain.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    LogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            return;
        }

        jwtToken = authHeader.substring(BEARER_TOKEN_PREFIX.length());
        TokenEntity storedToken = this.tokenRepository.findByToken(jwtToken)
                .orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            this.tokenRepository.save(storedToken);
        }
    }
}
