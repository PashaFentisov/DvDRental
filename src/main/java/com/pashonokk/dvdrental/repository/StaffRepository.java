package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country", "store", "store.address"})
    Page<Staff> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"address", "address.city", "address.city.country", "store", "store.address"})
    Optional<Staff> findById(@Param("id") Long id);

}
