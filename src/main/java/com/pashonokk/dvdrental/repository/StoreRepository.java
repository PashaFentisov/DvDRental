package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country"})
    Page<Store> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country"})
    Optional<Store> findById(@Param("id") Long id);

    @EntityGraph(attributePaths = "address")
    Optional<Store> getStoreById(@Param("id") Long id);
}
