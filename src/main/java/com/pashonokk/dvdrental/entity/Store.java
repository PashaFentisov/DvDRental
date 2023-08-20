package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Audited
public class Store {
    @Id
    private Long id;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true, optional = false)
    @MapsId
    @JoinColumn(name = "address_id")
    private Address address;
    private OffsetDateTime lastUpdate;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private Set<Staff> staff = new HashSet<>();
    @OneToMany(mappedBy = "store", orphanRemoval = true)
    @JsonIgnore
    @Setter(AccessLevel.PRIVATE)
    private List<Inventory> inventories = new ArrayList<>();
    private Boolean isDeleted;


    public void addAddress(Address address) {
        address.setStore(this);
        this.setAddress(address);
    }

}
