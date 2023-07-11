package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate lastUpdate;
    @OneToMany(mappedBy = "country", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(AccessLevel.PRIVATE)
    private List<City> cities = new ArrayList<>();

    public void addCity(City city) {
        this.cities.add(city);
        city.setCountry(this);
    }

    public void removeCity(City city) {
        this.cities.remove(city);
        city.setCountry(null);
    }
//todo зробити equals i hashcode тут і в сіті
    //todo переробити тут і в дто ліст на сет
}
