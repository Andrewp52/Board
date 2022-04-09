package com.pashenko.Board.services;

import com.pashenko.Board.entities.ConfirmToken;
import com.pashenko.Board.entities.User;
import com.pashenko.Board.exceptions.registration.ConfirmTokenNotFoundException;
import com.pashenko.Board.repositories.ConfirmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ConfirmTokenService {
    @Value("${token.signup.confirmation.expires}")
    private long expires;
    private ConfirmTokenRepository repository;

    @Autowired
    public ConfirmTokenService(ConfirmTokenRepository repository) {
        this.repository = repository;
    }

    public String generateAndGetConfirmToken(User user){
        ConfirmToken token = repository.save(
                new ConfirmToken(
                        user,
                        UUID.randomUUID().toString(),
                        LocalDateTime.now().plus(expires, ChronoUnit.MINUTES)
                )
        );
        return token.getUuid();
    }

    public List<ConfirmToken> getExpiredTokens(){
        return repository.findAllByExpirationDateBefore(LocalDateTime.now())
                .orElseGet(Collections::emptyList);
    }

    public void delete(ConfirmToken token) {
        repository.delete(token);
    }

    public ConfirmToken getTokenByUUID(String uuid) {
        return repository.findByUuid(uuid).orElseThrow(ConfirmTokenNotFoundException::new);
    }

    public void save(ConfirmToken token) {
        repository.save(token);
    }
}
