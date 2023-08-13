package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private OffsetDateTime rentalDate;
    private OffsetDateTime returnDate;
    private OffsetDateTime lastUpdate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private Staff staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @OneToOne(mappedBy = "rental", fetch = FetchType.LAZY)
    private Payment payment;


    public void addCustomer(Customer customer) {
        this.customer = customer;
        customer.getRentals().add(this);
    }
    public void addStaff(Staff staff) {
        this.staff = staff;
        staff.getRentals().add(this);
    }
    public void addInventory(Inventory inventory) {
        this.inventory = inventory;
        inventory.getRentals().add(this);
    }
    public void removePayment(Payment payments) {
        payment.setRental(null);
    }
}