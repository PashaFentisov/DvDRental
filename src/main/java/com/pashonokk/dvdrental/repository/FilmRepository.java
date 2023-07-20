package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @EntityGraph(attributePaths = "categories")
    Page<Film> findAll(Pageable pageable);
    @Query("select f from Film f left join fetch f.categories where f.id=:id")
    Optional<Film> findByIdWithCategories(@Param("id") Long id);
}
