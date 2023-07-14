package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findAll(Pageable pageable);

    @Query("Select c from Country c left join fetch c.cities where c.id=:id")
    Optional<Country> findByIdWithCities(Long id);
}
