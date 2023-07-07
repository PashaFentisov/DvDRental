package com.pashonokk.dvdrental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    private String name;
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "country", orphanRemoval = true)
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnore
    private Set<City> cities = new HashSet<>();

    public void addCity(City city) {
        this.cities.add(city);
        city.setCountry(this);
    }

    public void removeCity(City city) {
        this.cities.remove(city);
        city.setCountry(null);
    }
}
