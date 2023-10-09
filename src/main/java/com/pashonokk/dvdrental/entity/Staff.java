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
@ToString(exclude = "store")
@Audited
public class Staff {
    @Id
    private Long id;
    private String pictureUrl;
    private OffsetDateTime lastUpdate;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
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

    public Staff(String pictureUrl, OffsetDateTime lastUpdate, Boolean isDeleted) {
        this.pictureUrl = pictureUrl;
        this.lastUpdate = lastUpdate;
        this.isDeleted = isDeleted;
    }

    public void addStore(Store store) {
        store.getStaff().add(this);
        this.setStore(store);
    }
    public void addAddress(Address address) {
        address.setStaff(this);
        this.setAddress(address);
    }

    public void addUser(User user) {
        user.setStaff(this);
        this.setUser(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return address.equals(staff.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
