package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Long> {
    Page<Language> findAll(Pageable pageable);
//    @Query("select f from Film f left join fetch f.categories where f.id=:id")
//    Optional<Film> findByIdWithCategories(@Param("id") Long id);
}
