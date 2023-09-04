package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("Select p from Payment p where p.isClosed=false and p.customer.id=:id")
    List<Payment> findOpenPayments(@Param("id") Long id);
}
