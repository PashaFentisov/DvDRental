package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country"})
    Page<Store> findAll(Pageable pageable);

    @Query("select s from Store s left join fetch s.address left join fetch s.address.city left join fetch s.address.city.country where s.id=:id")
    Optional<Store> findByIdWithFullAddress(@Param("id") Long id);

    @Query("select s from Store s left join fetch s.address where s.id=:id")
    Optional<Store> findByIdWithAddress(@Param("id") Long id);
}
