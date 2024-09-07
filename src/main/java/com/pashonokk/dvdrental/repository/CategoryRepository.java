package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findAll(Pageable pageable);

    @Query("select c from Category c left join fetch c.films where c.id=:id")
    Optional<Category> findByIdWithFilms(@Param("id") Long id);

    @Query("select c from Category c left join fetch c.films where c.id in :ids")
    List<Category> findAllByIdAndFilms(@Param("ids") Iterable<Long> ids);
}
