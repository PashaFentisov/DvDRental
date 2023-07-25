package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findAll(Pageable pageable);

//    @Query("select f from Film f left join fetch f.categories left join fetch f.languages where f.id=:id")
//    Optional<Film> findByIdWithCategoriesAndLanguages(@Param("id") Long id);
//
//    @Query("select f from Film f left join fetch f.categories where f.id=:id")
//    Optional<Film> findByIdWithCategories(@Param("id") Long id);
//
//    @Query("select f from Film f left join fetch f.categories left join fetch f.languages lang where lang.name=:language")
//    List<Film> findByIdWithCategoriesAndLanguagesByLanguage(@Param("language") String language);
//
//    @Query("select f from Film f left join fetch f.languages lang where lang.id=:id")
//    List<Film> findByLanguageIdWithLanguages(@Param("id") Long id);
}
