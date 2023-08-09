package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

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
    private LocalDate lastUpdate;
    private LocalDate createDate;
    private boolean active;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;

    public void addAddress(Address address) {
        address.setCustomer(this);
        this.setAddress(address);
    }

    public void removeAddress(Address address) {
        this.setAddress(null);
        address.setCustomer(null);
    }
}
