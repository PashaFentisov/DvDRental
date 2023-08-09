package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    @Column(unique = true, nullable = false, updatable = false)
    private int postalCode;
    private LocalDate lastUpdate;
    @Column(unique = true, nullable = false, updatable = false)
    private String phone;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnore
    private Customer customer;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnore
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return postalCode == address.postalCode && phone.equals(address.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postalCode, phone);
    }
}
