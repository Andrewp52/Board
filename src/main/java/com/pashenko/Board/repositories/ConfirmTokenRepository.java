package com.pashenko.Board.repositories;

import com.pashenko.Board.entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConfirmTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<List<ConfirmationToken>> findAllByExpirationDateBefore(LocalDateTime dateTime);
    Optional<ConfirmationToken> findByUuid(String uuid);
}
