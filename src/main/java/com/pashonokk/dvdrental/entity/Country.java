package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String name;
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "country", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE)
    private Set<City> cities = new HashSet<>();

    public void addCity(City city) {
        this.cities.add(city);
        city.setCountry(this);
    }

    public void removeCity(City city) {
        this.cities.remove(city);
        city.setCountry(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return name.equals(country.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
