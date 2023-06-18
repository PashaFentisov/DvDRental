package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cities")
public class Country {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "country", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private Set<City> cities = new HashSet<>();
}
