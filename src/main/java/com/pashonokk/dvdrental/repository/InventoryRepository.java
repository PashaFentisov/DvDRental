package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @EntityGraph(attributePaths = {"store", "store.address", "film", "film.categories", "film.languages"})
    Page<Inventory> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"store", "store.address", "film", "film.categories", "film.languages"})
    Optional<Inventory> findById(Long id);
    @Query("Select i from Inventory i where i.film.id=:filmId and i.store.id=:storeId")
    Optional<Inventory> findByFilmAndStore(@Param("filmId") Long filmId, @Param("storeId")Long storeId);
}
