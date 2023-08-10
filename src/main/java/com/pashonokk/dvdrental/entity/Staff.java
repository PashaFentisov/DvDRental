package com.pashonokk.dvdrental.entity;

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
public class Staff {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    @Column(unique = true, nullable = false, updatable = false)
    private String username;
    private String password;
    private String pictureUrl;
    private LocalDate lastUpdate;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void addStore(Store store) {
        store.getStaff().add(this);
        this.setStore(store);
    }

    public void removeStore(Store store) {
        store.getStaff().remove(this);
        this.setStore(null);
    }

    public void addAddress(Address address) {
        address.setStaff(this);
        this.setAddress(address);
    }

    public void removeAddress(Address address) {
        this.setAddress(null);
        address.setStaff(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return username.equals(staff.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
