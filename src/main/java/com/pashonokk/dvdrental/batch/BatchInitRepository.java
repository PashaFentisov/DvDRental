package com.pashonokk.dvdrental.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BatchInitRepository extends JpaRepository<BatchInit, Long> {
    @Query("select max(b.version) from BatchInit b")
    Long findLastVersion();
}
