package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @EntityGraph(attributePaths = "roles")
    Page<Permission> findAll(Pageable pageable);

    @Query("select p from Permission p left join fetch p.roles where p.id =:id")
    Optional<Permission> findById(@Param("id") Long id);
}
