package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private OffsetDateTime lastUpdate;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @OneToMany(mappedBy = "staff")
    @Setter(AccessLevel.PRIVATE)
    private List<Rental> rentals = new ArrayList<>();

    @OneToMany(mappedBy = "staff")
    @Setter(AccessLevel.PRIVATE)
    private List<Payment> payments = new ArrayList<>();
    private Boolean isDeleted;

    public void addStore(Store store) {
        store.getStaff().add(this);
        this.setStore(store);
    }


    public void addAddress(Address address) {
        address.setStaff(this);
        this.setAddress(address);
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
