package com.pashonokk.dvdrental.repository;


import com.pashonokk.dvdrental.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("Select a from Address a left join fetch a.city left join fetch a.city.country where a.id=:id")
    Optional<Address> findByIdAndCityAndCountry(Long id);
}
