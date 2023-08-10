package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    @EntityGraph(attributePaths = {"categories", "languages"})
    Page<Film> findAll(Pageable pageable);

    @Query("select f from Film f left join fetch f.categories left join fetch f.languages where f.id=:id")
    Optional<Film> findByIdWithCategoriesAndLanguages(@Param("id") Long id);

    @Query("select f from Film f left join fetch f.categories where f.id=:id")
    Optional<Film> findByIdWithCategories(@Param("id") Long id);

    @Query("select f from Film f left join fetch f.categories left join fetch f.languages lang where lang.name=:language")
    List<Film> findByIdWithCategoriesAndLanguagesByLanguage(@Param("language") String language);

    @Query("select f from Film f left join fetch f.actors a where f.id=:id")
    Optional<Film> findByIdWithActors(@Param("id") Long id);
    @EntityGraph(attributePaths = {"inventories", "inventories.store", "inventories.store.address"})
    Optional<Film> getFilmById(Long id);
}
