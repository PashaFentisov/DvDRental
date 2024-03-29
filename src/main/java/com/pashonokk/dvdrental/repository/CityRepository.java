package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    @EntityGraph(attributePaths = "country")
    Page<City> findAll(Pageable pageable);
    @Query("Select c from City c left join fetch c.addresses where c.id=:id")
    Optional<City> findByIdWithAddresses(@Param("id") Long id);

    @Query("Select c from City c left join fetch c.addresses left join fetch c.country where c.id=:id")
    Optional<City> findByIdWithAddressesAndCountry(@Param("id") Long id);

    @Query("Select c from City c left join fetch c.country where c.id=:id")
    Optional<City> findByIdWithCountry(@Param("id") Long id);
}
