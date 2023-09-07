package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"store", "staff", "customer"})
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
    private OffsetDateTime lastUpdate;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnore
    private Customer customer;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnore
    private Store store;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnore
    private Staff staff;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "address")
    private Set<Phone> phones = new HashSet<>();
    private Boolean isDeleted;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return postalCode == address.postalCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(postalCode);
    }
}
