package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country"})
    Page<Customer> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country"})
    Optional<Customer> findCustomerById(@Param("id") Long id);
    @EntityGraph(attributePaths = "address")
    Optional<Customer> findById(@Param("id") Long id);
}

