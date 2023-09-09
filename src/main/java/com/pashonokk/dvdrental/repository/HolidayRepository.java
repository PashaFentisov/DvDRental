package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    @Query("SELECT COUNT(*) FROM Holiday h WHERE h.date BETWEEN :startDate AND :endDate")
    int countHolidayBetweenDates(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);
}
