package com.pashenko.Board.repositories;

import com.pashenko.Board.entities.ConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken, Long> {
    Optional<List<ConfirmToken>> findAllByExpirationDateBefore(LocalDateTime dateTime);
    Optional<ConfirmToken> findByUuid(String uuid);
}
