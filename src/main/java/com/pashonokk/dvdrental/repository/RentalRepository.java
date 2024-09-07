package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @EntityGraph(attributePaths = {"customer.address", "staff.address", "staff.store.address","inventory.film.languages", "inventory.film.categories", "inventory.store.address"})
    Optional<Rental> findById(Long id);
    @EntityGraph(attributePaths = {"customer.address", "staff.address", "staff.store.address","inventory.film.languages", "inventory.film.categories", "inventory.store.address"})
    Page<Rental> findAll(Pageable pageable);
}
