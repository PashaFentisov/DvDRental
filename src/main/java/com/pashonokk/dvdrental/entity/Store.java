package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
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
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "store")
    @JsonIgnore
    private Set<Staff> staff = new HashSet<>();
    @OneToMany(mappedBy = "store", orphanRemoval = true)
    @JsonIgnore
    private List<Inventory> inventories = new ArrayList<>();

    public void addAddress(Address address) {
        address.setStore(this);
        this.setAddress(address);
    }

    public void removeAddress(Address address) {
        this.setAddress(null);
        address.setStore(null);
    }

    public void removeInventories(List<Inventory> inventories) {
        for(Inventory inventory: inventories){
            inventory.setStore(null);
        }
    }

}
