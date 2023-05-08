package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    @Column(name = "last_update")
    private LocalDate lastUpdate;
    @Column(name = "create_date")
    private LocalDate createDate;
    private boolean active;

    public Customer(String firstName, String lastName, String email, LocalDate lastUpdate, LocalDate createDate, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.lastUpdate = lastUpdate;
        this.createDate = createDate;
        this.active = active;
    }
}
