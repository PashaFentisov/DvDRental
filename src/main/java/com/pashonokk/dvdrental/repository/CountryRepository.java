package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Page<Country> findAll(Pageable pageable);

}
