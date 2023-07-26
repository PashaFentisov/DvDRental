package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Page<Actor> findAll(Pageable pageable);

    @Query("select a from Actor a join fetch a.films where a.id=:id")
    Optional<Actor> findByIdWithFilms(@Param("id") Long id);

    @Query("select a from Actor a left join fetch a.films where a.id in :ids")
    List<Actor> findAllByIdAndFilms(@Param("ids") Iterable<Long> ids);

}
