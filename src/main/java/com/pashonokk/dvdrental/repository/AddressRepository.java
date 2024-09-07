package com.pashonokk.dvdrental.repository;


import com.pashonokk.dvdrental.entity.Address;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    @EntityGraph(attributePaths = {"city", "city.country"})
    Optional<Address> findById(Long id);
}
