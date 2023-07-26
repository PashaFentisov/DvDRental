package com.pashonokk.dvdrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, updatable = false)
    private String name;
    private LocalDate lastUpdate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "languages")
    @Setter(AccessLevel.PRIVATE)
    private Set<Film> films = new HashSet<>();
    public void removeFilms(Set<Film> films) {
        for(Film film: films){
            film.removeLanguage(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return name.equals(language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
