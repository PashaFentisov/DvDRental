package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    Page<Language> findAll(Pageable pageable);
    @Query("select l from Language l join fetch l.films where l.id=:id")
    Optional<Language> findByIdWithFilms(@Param("id") Long id);
}
