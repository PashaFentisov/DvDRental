package com.pashonokk.dvdrental.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private OffsetDateTime paymentDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rental_id")
    private Rental rental;

    public void addCustomer(Customer customer) {
        this.customer = customer;
        customer.getPayments().add(this);
    }
    public void addStaff(Staff staff) {
        this.staff = staff;
        staff.getPayments().add(this);
    }
    public void addRental(Rental rental) {
        this.rental = rental;
        rental.setPayment(this);
    }
}
