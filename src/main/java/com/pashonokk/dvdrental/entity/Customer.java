package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private OffsetDateTime lastUpdate;
    private OffsetDateTime createDate;
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "customer")
    @Setter(AccessLevel.PRIVATE)
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    @Setter(AccessLevel.PRIVATE)
    private List<Payment> payments = new ArrayList<>();

    public void addAddress(Address address) {
        address.setCustomer(this);
        this.setAddress(address);
    }

    public void removeAddress(Address address) {
        this.setAddress(null);
        address.setCustomer(null);
    }
    public void removeRentals(List<Rental> rentals) {
        for(Rental rental: rentals){
            rental.setCustomer(null);
        }
    }
    public void removePayments(List<Payment> payments) {
        for(Payment payment: payments){
            payment.setCustomer(null);
        }
    }

}
