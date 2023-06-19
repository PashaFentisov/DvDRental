package com.pashonokk.dvdrental.entity;

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
    @OneToMany(mappedBy = "country", cascade = {CascadeType.ALL})
    @Setter(AccessLevel.PRIVATE)
    private Set<City> cities = new HashSet<>();

    public void addCity(City city){
        this.cities.add(city);
        city.setCountry(this);
    }
}
