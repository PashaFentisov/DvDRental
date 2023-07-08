package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "customer")
public class Address {
    @Id
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    private int postalCode;
    private LocalDate lastUpdate;
    private String phone;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
}
