package com.example.distributed_database_project_server.authentication;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.distributed_database_project_server.domain.entity.AccountEntity;
import com.example.distributed_database_project_server.domain.entity.TokenEntity;
import com.example.distributed_database_project_server.domain.repository.AccountRepository;
import com.example.distributed_database_project_server.domain.repository.TokenRepository;

import jakarta.transaction.Transactional;

@Service
class AuthenticationService {

    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final JwtPublicService jwtService;
    private final AuthenticationManager authenticationManager;

    AuthenticationService(
            AccountRepository accountRepository,
            TokenRepository tokenRepository,
            JwtPublicService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.accountRepository = accountRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    AuthenticationResponse login(LoginRequest request) {
        AccountEntity user = this.accountRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

        String jwt = this.authenticate(user, request.getPassword());

        return new AuthenticationResponse("Login successful.", jwt);
    }

    private String authenticate(AccountEntity user, String originalPassword) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        originalPassword
                )
        );

        String jwt = this.jwtService.generateToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(jwt, user);

        return jwt;
    }

    private void revokeAllUserTokens(AccountEntity user) {
        List<TokenEntity> validUserTokens = this.tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        for (TokenEntity token : validUserTokens) {
            token.setExpired(true);
            token.setRevoked(true);
        }

        this.tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(String jwt, AccountEntity user) {
        this.tokenRepository.save(new TokenEntity(jwt, false, false, user));
    }
}
