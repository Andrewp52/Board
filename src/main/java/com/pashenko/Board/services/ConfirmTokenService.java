package com.pashenko.Board.services;

import com.pashenko.Board.entities.ConfirmationToken;
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

    public ConfirmationToken generateAndGetConfirmToken(User user){
        return repository.save(
                new ConfirmationToken(
                        user,
                        UUID.randomUUID().toString(),
                        LocalDateTime.now().plus(expires, ChronoUnit.MINUTES)
                )
        );
    }

    public List<ConfirmationToken> getExpiredTokens(){
        return repository.findAllByExpirationDateBefore(LocalDateTime.now())
                .orElseGet(Collections::emptyList);
    }

    public void delete(ConfirmationToken token) {
        repository.delete(token);
    }

    public ConfirmationToken getTokenByUUID(String uuid) {
        return repository.findByUuid(uuid).orElseThrow(ConfirmTokenNotFoundException::new);
    }

    public void save(ConfirmationToken token) {
        repository.save(token);
    }


}
