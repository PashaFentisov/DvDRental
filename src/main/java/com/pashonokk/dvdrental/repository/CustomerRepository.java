package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @EntityGraph(attributePaths = "address")
    Page<Customer> findAll(Pageable pageable);

    @Query("select c from Customer c left join fetch c.address where c.id=:id")
    Optional<Customer> findByIdWithAddress(@Param("id") Long id);
}
