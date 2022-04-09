package com.pashenko.Board.repositories;

import com.pashenko.Board.entities.Offer;
import com.pashenko.Board.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface OfferRepository extends PagingAndSortingRepository<Offer, Long> {
    Page<Offer> findByUser(User user, Pageable pageable);
    Optional<Offer> findById(Long id);
}
